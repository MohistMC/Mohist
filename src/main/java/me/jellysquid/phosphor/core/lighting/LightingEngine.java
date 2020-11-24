package me.jellysquid.phosphor.core.lighting;

import me.jellysquid.phosphor.api.IChunkLighting;
import me.jellysquid.phosphor.api.ILightingEngine;
import me.jellysquid.phosphor.core.collections.PooledLongQueue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.concurrent.locks.ReentrantLock;

public class LightingEngine implements ILightingEngine {
    private static final int MAX_SCHEDULED_COUNT = 1 << 22;

    private static final int MAX_LIGHT = 15;

    private final Thread ownedThread = Thread.currentThread();

    private final World world;

    private final Profiler profiler;

    //Layout of longs: [padding(4)] [y(8)] [x(26)] [z(26)]
    private final PooledLongQueue[] queuedLightUpdates = new PooledLongQueue[EnumSkyBlock.values().length];

    //Layout of longs: see above
    private final PooledLongQueue[] queuedDarkenings = new PooledLongQueue[MAX_LIGHT + 1];
    private final PooledLongQueue[] queuedBrightenings = new PooledLongQueue[MAX_LIGHT + 1];

    //Layout of longs: [newLight(4)] [pos(60)]
    private final PooledLongQueue initialBrightenings;
    //Layout of longs: [padding(4)] [pos(60)]
    private final PooledLongQueue initialDarkenings;

    private boolean updating = false;

    //Layout parameters
    //Length of bit segments
    private static final int
            lX = 26,
            lY = 8,
            lZ = 26,
            lL = 4;

    //Bit segment shifts/positions
    private static final int
            sZ = 0,
            sX = sZ + lZ,
            sY = sX + lX,
            sL = sY + lY;

    //Bit segment masks
    private static final long
            mX = (1L << lX) - 1,
            mY = (1L << lY) - 1,
            mZ = (1L << lZ) - 1,
            mL = (1L << lL) - 1,
            mPos = (mY << sY) | (mX << sX) | (mZ << sZ);

    //Bit to check whether y had overflow
    private static final long yCheck = 1L << (sY + lY);

    private static final long[] neighborShifts = new long[6];

    static {
        for (int i = 0; i < 6; ++i) {
            final Vec3i offset = EnumFacing.VALUES[i].getDirectionVec();
            neighborShifts[i] = ((long) offset.getY() << sY) | ((long) offset.getX() << sX) | ((long) offset.getZ() << sZ);
        }
    }

    //Mask to extract chunk identifier
    private static final long mChunk = ((mX >> 4) << (4 + sX)) | ((mZ >> 4) << (4 + sZ));

    //Iteration state data
    //Cache position to avoid allocation of new object each time
    private final MutableBlockPos curPos = new MutableBlockPos();
    private Chunk curChunk;
    private long curChunkIdentifier;
    private long curData;

    //Cached data about neighboring blocks (of tempPos)
    private boolean isNeighborDataValid = false;

    private final NeighborInfo[] neighborInfos = new NeighborInfo[6];
    private PooledLongQueue.LongQueueIterator queueIt;

    private final ReentrantLock lock = new ReentrantLock();

    public LightingEngine(final World world) {
        this.world = world;
        this.profiler = world.profiler;

        PooledLongQueue.Pool pool = new PooledLongQueue.Pool();

        this.initialBrightenings = new PooledLongQueue(pool);
        this.initialDarkenings = new PooledLongQueue(pool);

        for (int i = 0; i < EnumSkyBlock.values().length; ++i) {
            this.queuedLightUpdates[i] = new PooledLongQueue(pool);
        }

        for (int i = 0; i < this.queuedDarkenings.length; ++i) {
            this.queuedDarkenings[i] = new PooledLongQueue(pool);
        }

        for (int i = 0; i < this.queuedBrightenings.length; ++i) {
            this.queuedBrightenings[i] = new PooledLongQueue(pool);
        }

        for (int i = 0; i < this.neighborInfos.length; ++i) {
            this.neighborInfos[i] = new NeighborInfo();
        }
    }

    /**
     * Schedules a light update for the specified light type and position to be processed later by {@link ILightingEngine#processLightUpdatesForType(EnumSkyBlock)}
     */
    @Override
    public void scheduleLightUpdate(final EnumSkyBlock lightType, final BlockPos pos) {
        this.acquireLock();

        try {
            this.scheduleLightUpdate(lightType, encodeWorldCoord(pos));
        } finally {
            this.releaseLock();
        }
    }

    /**
     * Schedules a light update for the specified light type and position to be processed later by {@link ILightingEngine#processLightUpdates()}
     */
    private void scheduleLightUpdate(final EnumSkyBlock lightType, final long pos) {
        final PooledLongQueue queue = this.queuedLightUpdates[lightType.ordinal()];

        queue.add(pos);

        //make sure there are not too many queued light updates
        if (queue.size() >= MAX_SCHEDULED_COUNT) {
            this.processLightUpdatesForType(lightType);
        }
    }

    /**
     * Calls {@link ILightingEngine#processLightUpdatesForType(EnumSkyBlock)} for both light types
     *
     */
    @Override
    public void processLightUpdates() {
        this.processLightUpdatesForType(EnumSkyBlock.SKY);
        this.processLightUpdatesForType(EnumSkyBlock.BLOCK);
    }

    /**
     * Processes light updates of the given light type
     */
    @Override
    public void processLightUpdatesForType(final EnumSkyBlock lightType) {
        // We only want to perform updates if we're being called from a tick event on the client
        // There are many locations in the client code which will end up making calls to this method, usually from
        // other threads.
        if (this.world.isRemote && !this.isCallingFromMainThread()) {
            return;
        }

        final PooledLongQueue queue = this.queuedLightUpdates[lightType.ordinal()];

        // Quickly check if the queue is empty before we acquire a more expensive lock.
        if (queue.isEmpty()) {
            return;
        }

        this.acquireLock();

        try {
            this.processLightUpdatesForTypeInner(lightType, queue);
        } finally {
            this.releaseLock();
        }
    }

    @SideOnly(Side.CLIENT)
    private boolean isCallingFromMainThread() {
        return Minecraft.getMinecraft().isCallingFromMinecraftThread();
    }

    private void acquireLock() {
        if (!this.lock.tryLock()) {
            // If we cannot lock, something has gone wrong... Only one thread should ever acquire the lock.
            // Validate that we're on the right thread immediately so we can gather information.
            // It is NEVER valid to call World methods from a thread other than the owning thread of the world instance.
            // Users can safely disable this warning, however it will not resolve the issue.
//            if (LightingEnginePlugin.ENABLE_ILLEGAL_THREAD_ACCESS_WARNINGS) {
//                Thread current = Thread.currentThread();
//
//                if (current != this.ownedThread) {
//                    IllegalAccessException e = new IllegalAccessException(String.format("World is owned by '%s' (ID: %s)," +
//                                    " but was accessed from thread '%s' (ID: %s)",
//                            this.ownedThread.getName(), this.ownedThread.getId(), current.getName(), current.getId()));
//
//                    PhosphorMod.LOGGER.warn(
//                            "Something (likely another mod) has attempted to modify the world's state from the wrong thread!\n" +
//                                    "This is *bad practice* and can cause severe issues in your game. Phosphor has done as best as it can to mitigate this violation," +
//                                    " but it may negatively impact performance or introduce stalls.\nIn a future release, this violation may result in a hard crash instead" +
//                                    " of the current soft warning. You should report this issue to our issue tracker with the following stacktrace information.\n(If you are" +
//                                    " aware you have misbehaving mods and cannot resolve this issue, you can safely disable this warning by setting" +
//                                    " `enable_illegal_thread_access_warnings` to `false` in Phosphor's configuration file for the time being.)", e);
//
//                }

//            }

            // Wait for the lock to be released. This will likely introduce unwanted stalls, but will mitigate the issue.
            this.lock.lock();
        }
    }

    private void releaseLock() {
        this.lock.unlock();
    }

    private void processLightUpdatesForTypeInner(final EnumSkyBlock lightType, final PooledLongQueue queue) {
        //avoid nested calls
        if (this.updating) {
            throw new IllegalStateException("Already processing updates!");
        }

        this.updating = true;

        this.curChunkIdentifier = -1; //reset chunk cache

        this.profiler.startSection("lighting");

        this.profiler.startSection("checking");

        this.queueIt = queue.iterator();

        //process the queued updates and enqueue them for further processing
        while (this.nextItem()) {
            if (this.curChunk == null) {
                continue;
            }

            final int oldLight = this.getCursorCachedLight(lightType);
            final int newLight = this.calculateNewLightFromCursor(lightType);

            if (oldLight < newLight) {
                //don't enqueue directly for brightening in order to avoid duplicate scheduling
                this.initialBrightenings.add(((long) newLight << sL) | this.curData);
            } else if (oldLight > newLight) {
                //don't enqueue directly for darkening in order to avoid duplicate scheduling
                this.initialDarkenings.add(this.curData);
            }
        }

        this.queueIt = this.initialBrightenings.iterator();

        while (this.nextItem()) {
            final int newLight = (int) (this.curData >> sL & mL);

            if (newLight > this.getCursorCachedLight(lightType)) {
                //Sets the light to newLight to only schedule once. Clear leading bits of curData for later
                this.enqueueBrightening(this.curPos, this.curData & mPos, newLight, this.curChunk, lightType);
            }
        }

        this.queueIt = this.initialDarkenings.iterator();

        while (this.nextItem()) {
            final int oldLight = this.getCursorCachedLight(lightType);

            if (oldLight != 0) {
                //Sets the light to 0 to only schedule once
                this.enqueueDarkening(this.curPos, this.curData, oldLight, this.curChunk, lightType);
            }
        }

        this.profiler.endSection();

        //Iterate through enqueued updates (brightening and darkening in parallel) from brightest to darkest so that we only need to iterate once
        for (int curLight = MAX_LIGHT; curLight >= 0; --curLight) {
            this.profiler.startSection("darkening");

            this.queueIt = this.queuedDarkenings[curLight].iterator();

            while (this.nextItem()) {
                if (this.getCursorCachedLight(lightType) >= curLight) //don't darken if we got brighter due to some other change
                {
                    continue;
                }

                final IBlockState state = LightingEngineHelpers.posToState(this.curPos, this.curChunk);
                final int luminosity = this.getCursorLuminosity(state, lightType);
                final int opacity; //if luminosity is high enough, opacity is irrelevant

                if (luminosity >= MAX_LIGHT - 1) {
                    opacity = 1;
                } else {
                    opacity = this.getPosOpacity(this.curPos, state);
                }

                //only darken neighbors if we indeed became darker
                if (this.calculateNewLightFromCursor(luminosity, opacity, lightType) < curLight) {
                    //need to calculate new light value from neighbors IGNORING neighbors which are scheduled for darkening
                    int newLight = luminosity;

                    this.fetchNeighborDataFromCursor(lightType);

                    for (NeighborInfo info : this.neighborInfos) {
                        final Chunk nChunk = info.chunk;

                        if (nChunk == null) {
                            continue;
                        }

                        final int nLight = info.light;

                        if (nLight == 0) {
                            continue;
                        }

                        final MutableBlockPos nPos = info.pos;

                        if (curLight - this.getPosOpacity(nPos, LightingEngineHelpers.posToState(nPos, info.section)) >= nLight) //schedule neighbor for darkening if we possibly light it
                        {
                            this.enqueueDarkening(nPos, info.key, nLight, nChunk, lightType);
                        } else //only use for new light calculation if not
                        {
                            //if we can't darken the neighbor, no one else can (because of processing order) -> safe to let us be illuminated by it
                            newLight = Math.max(newLight, nLight - opacity);
                        }
                    }

                    //schedule brightening since light level was set to 0
                    this.enqueueBrighteningFromCursor(newLight, lightType);
                } else //we didn't become darker, so we need to re-set our initial light value (was set to 0) and notify neighbors
                {
                    this.enqueueBrighteningFromCursor(curLight, lightType); //do not spread to neighbors immediately to avoid scheduling multiple times
                }
            }

            this.profiler.endStartSection("brightening");

            this.queueIt = this.queuedBrightenings[curLight].iterator();

            while (this.nextItem()) {
                final int oldLight = this.getCursorCachedLight(lightType);

                if (oldLight == curLight) //only process this if nothing else has happened at this position since scheduling
                {
                    this.world.notifyLightSet(this.curPos);

                    if (curLight > 1) {
                        this.spreadLightFromCursor(curLight, lightType);
                    }
                }
            }

            this.profiler.endSection();
        }

        this.profiler.endSection();

        this.updating = false;
    }

    /**
     * Gets data for neighbors of <code>curPos</code> and saves the results into neighbor state data members. If a neighbor can't be accessed/doesn't exist, the corresponding entry in <code>neighborChunks</code> is <code>null</code> - others are not reset
     */
    private void fetchNeighborDataFromCursor(final EnumSkyBlock lightType) {
        //only update if curPos was changed
        if (this.isNeighborDataValid) {
            return;
        }

        this.isNeighborDataValid = true;

        for (int i = 0; i < this.neighborInfos.length; ++i) {
            NeighborInfo info = this.neighborInfos[i];

            final long nLongPos = info.key = this.curData + neighborShifts[i];

            if ((nLongPos & yCheck) != 0) {
                info.chunk = null;
                info.section = null;
                continue;
            }

            final MutableBlockPos nPos = decodeWorldCoord(info.pos, nLongPos);

            final Chunk nChunk;

            if ((nLongPos & mChunk) == this.curChunkIdentifier) {
                nChunk = info.chunk = this.curChunk;
            } else {
                nChunk = info.chunk = this.getChunk(nPos);
            }

            if (nChunk != null) {
                ExtendedBlockStorage nSection = nChunk.getBlockStorageArray()[nPos.getY() >> 4];

                info.light = getCachedLightFor(nChunk, nSection, nPos, lightType);
                info.section = nSection;
            }
        }
    }


    private static int getCachedLightFor(Chunk chunk, ExtendedBlockStorage storage, BlockPos pos, EnumSkyBlock type) {
        int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;

        if (storage == Chunk.NULL_BLOCK_STORAGE) {
            if (type == EnumSkyBlock.SKY && chunk.canSeeSky(pos)) {
                return type.defaultLightValue;
            } else {
                return 0;
            }
        } else if (type == EnumSkyBlock.SKY) {
            if (!chunk.getWorld().provider.hasSkyLight()) {
                return 0;
            } else {
                return storage.getSkyLight(i, j & 15, k);
            }
        } else {
            if (type == EnumSkyBlock.BLOCK) {
                return storage.getBlockLight(i, j & 15, k);
            } else {
                return type.defaultLightValue;
            }
        }
    }


    private int calculateNewLightFromCursor(final EnumSkyBlock lightType) {
        final IBlockState state = LightingEngineHelpers.posToState(this.curPos, this.curChunk);

        final int luminosity = this.getCursorLuminosity(state, lightType);
        final int opacity;

        if (luminosity >= MAX_LIGHT - 1) {
            opacity = 1;
        } else {
            opacity = this.getPosOpacity(this.curPos, state);
        }

        return this.calculateNewLightFromCursor(luminosity, opacity, lightType);
    }

    private int calculateNewLightFromCursor(final int luminosity, final int opacity, final EnumSkyBlock lightType) {
        if (luminosity >= MAX_LIGHT - opacity) {
            return luminosity;
        }

        int newLight = luminosity;

        this.fetchNeighborDataFromCursor(lightType);

        for (NeighborInfo info : this.neighborInfos) {
            if (info.chunk == null) {
                continue;
            }

            final int nLight = info.light;

            newLight = Math.max(nLight - opacity, newLight);
        }

        return newLight;
    }

    private void spreadLightFromCursor(final int curLight, final EnumSkyBlock lightType) {
        this.fetchNeighborDataFromCursor(lightType);

        for (NeighborInfo info : this.neighborInfos) {
            final Chunk nChunk = info.chunk;

            if (nChunk == null) {
                continue;
            }

            final int newLight = curLight - this.getPosOpacity(info.pos, LightingEngineHelpers.posToState(info.pos, info.section));

            if (newLight > info.light) {
                this.enqueueBrightening(info.pos, info.key, newLight, nChunk, lightType);
            }
        }
    }

    private void enqueueBrighteningFromCursor(final int newLight, final EnumSkyBlock lightType) {
        this.enqueueBrightening(this.curPos, this.curData, newLight, this.curChunk, lightType);
    }

    /**
     * Enqueues the pos for brightening and sets its light value to <code>newLight</code>
     */
    private void enqueueBrightening(final BlockPos pos, final long longPos, final int newLight, final Chunk chunk, final EnumSkyBlock lightType) {
        this.queuedBrightenings[newLight].add(longPos);

        chunk.setLightFor(lightType, pos, newLight);
    }

    /**
     * Enqueues the pos for darkening and sets its light value to 0
     */
    private void enqueueDarkening(final BlockPos pos, final long longPos, final int oldLight, final Chunk chunk, final EnumSkyBlock lightType) {
        this.queuedDarkenings[oldLight].add(longPos);

        chunk.setLightFor(lightType, pos, 0);
    }

    private static MutableBlockPos decodeWorldCoord(final MutableBlockPos pos, final long longPos) {
        final int posX = (int) (longPos >> sX & mX) - (1 << lX - 1);
        final int posY = (int) (longPos >> sY & mY);
        final int posZ = (int) (longPos >> sZ & mZ) - (1 << lZ - 1);

        return pos.setPos(posX, posY, posZ);
    }

    private static long encodeWorldCoord(final BlockPos pos) {
        return encodeWorldCoord(pos.getX(), pos.getY(), pos.getZ());
    }

    private static long encodeWorldCoord(final long x, final long y, final long z) {
        return (y << sY) | (x + (1 << lX - 1) << sX) | (z + (1 << lZ - 1) << sZ);
    }

    private static int ITEMS_PROCESSED = 0, CHUNKS_FETCHED = 0;

    /**
     * Polls a new item from <code>curQueue</code> and fills in state data members
     *
     * @return If there was an item to poll
     */
    private boolean nextItem() {
        if (!this.queueIt.hasNext()) {
            this.queueIt.finish();
            this.queueIt = null;

            return false;
        }

        this.curData = this.queueIt.next();
        this.isNeighborDataValid = false;

        decodeWorldCoord(this.curPos, this.curData);

        final long chunkIdentifier = this.curData & mChunk;

        if (this.curChunkIdentifier != chunkIdentifier) {
            this.curChunk = this.getChunk(this.curPos);
            this.curChunkIdentifier = chunkIdentifier;
            CHUNKS_FETCHED++;
        }

        ITEMS_PROCESSED++;

        return true;
    }

    private int getCursorCachedLight(final EnumSkyBlock lightType) {
        return ((IChunkLighting) this.curChunk).getCachedLightFor(lightType, this.curPos);
    }

    /**
     * Calculates the luminosity for <code>curPos</code>, taking into account <code>lightType</code>
     */
    private int getCursorLuminosity(final IBlockState state, final EnumSkyBlock lightType) {
        if (lightType == EnumSkyBlock.SKY) {
            if (this.curChunk.canSeeSky(this.curPos)) {
                return EnumSkyBlock.SKY.defaultLightValue;
            } else {
                return 0;
            }
        }

        return MathHelper.clamp(state.getLightValue(this.world, this.curPos), 0, MAX_LIGHT);
    }

    private int getPosOpacity(final BlockPos pos, final IBlockState state) {
        return MathHelper.clamp(state.getLightOpacity(this.world, pos), 1, MAX_LIGHT);
    }

    private Chunk getChunk(final BlockPos pos) {
        return this.world.getChunkProvider().getLoadedChunk(pos.getX() >> 4, pos.getZ() >> 4);
    }

    private static class NeighborInfo {
        Chunk chunk;
        ExtendedBlockStorage section;

        int light;

        long key;

        final MutableBlockPos pos = new MutableBlockPos();
    }
}