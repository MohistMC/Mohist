package com.destroystokyo.paper.server.ticklist;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import net.minecraft.addons.server.MCUtil;
import net.minecraft.block.BlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.TickPriority;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerTickList;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class PaperTickList<T> extends ServerTickList<T> { // extend to avoid breaking ABI

    // in the order the state is expected to change (mostly)
    public static final int STATE_UNSCHEDULED = 1 << 0;
    public static final int STATE_SCHEDULED = 1 << 1; // scheduled for some tick
    public static final int STATE_PENDING_TICK = 1 << 2; // for this tick
    public static final int STATE_TICKING = 1 << 3;
    public static final int STATE_TICKED = 1 << 4; // after this, it gets thrown back to unscheduled
    public static final int STATE_CANCELLED_TICK = 1 << 5; // still gets moved to unscheduled after tick

    private static final int SHORT_SCHEDULE_TICK_THRESHOLD = 20 * 20 + 1; // 20 seconds

    private final ServerWorld world;
    private final Predicate<T> excludeFromScheduling;
    private final Function<T, ResourceLocation> getMinecraftKeyFrom;
    //private final Function<MinecraftKey, T> getObjectFronMinecraftKey;
    private final Consumer<NextTickListEntry<T>> tickFunction;

    private final co.aikar.timings.Timing timingCleanup; // Paper
    private final co.aikar.timings.Timing timingTicking; // Paper
    private final co.aikar.timings.Timing timingFinished;

    // note: remove ops / add ops suck on fastutil, a chained hashtable implementation would work better, but Long...
    // try to alleviate with a very small load factor
    private final Long2ObjectOpenHashMap<ArrayList<NextTickListEntry<T>>> entriesByBlock = new Long2ObjectOpenHashMap<>(1024, 0.25f);
    private final Long2ObjectOpenHashMap<ObjectRBTreeSet<NextTickListEntry<T>>> entriesByChunk = new Long2ObjectOpenHashMap<>(1024, 0.25f);
    private final Long2ObjectOpenHashMap<ArrayList<NextTickListEntry<T>>> pendingChunkTickLoad = new Long2ObjectOpenHashMap<>(1024, 0.5f);

    // fastutil has O(1) first/last while TreeMap/TreeSet are log(n)
    private final ObjectRBTreeSet<NextTickListEntry<T>> longScheduled = new ObjectRBTreeSet<>(TickListServerInterval.ENTRY_COMPARATOR);

    private final ArrayDeque<NextTickListEntry<T>> toTickThisTick = new ArrayDeque<>();

    private final TickListServerInterval<T>[] shortScheduled = new TickListServerInterval[SHORT_SCHEDULE_TICK_THRESHOLD];

    {
        for (int i = 0, len = this.shortScheduled.length; i < len; ++i) {
            this.shortScheduled[i] = new TickListServerInterval<>();
        }
    }

    private int shortScheduledIndex;

    private long currentTick;

    private static final boolean WARN_ON_EXCESSIVE_DELAY = Boolean.getBoolean("paper.ticklist-warn-on-excessive-delay");
    private static final long EXCESSIVE_DELAY_THRESHOLD = Long.getLong("paper.ticklist-excessive-delay-threshold", 60 * 20).longValue(); // 1 min dfl

    // assume index < length
    private static int getWrappedIndex(final int start, final int length, final int index) {
        final int next = start + index;
        return next < length ? next : next - length;
    }

    private static int getNextIndex(final int curr, final int length) {
        final int next = curr + 1;
        return next < length ? next : 0;
    }

    public PaperTickList(final ServerWorld world, final Predicate<T> excludeFromScheduling, final Function<T, ResourceLocation> getMinecraftKeyFrom,
                         final Consumer<NextTickListEntry<T>> tickFunction, final String timingsType) {
        super(world, excludeFromScheduling, getMinecraftKeyFrom, tickFunction, timingsType);
        this.world = world;
        this.excludeFromScheduling = excludeFromScheduling;
        this.getMinecraftKeyFrom = getMinecraftKeyFrom;
        this.tickFunction = tickFunction;
        this.timingCleanup = co.aikar.timings.WorldTimingsHandler.getTickList(world, timingsType + " - Cleanup"); // Paper
        this.timingTicking = co.aikar.timings.WorldTimingsHandler.getTickList(world, timingsType + " - Ticking"); // Paper
        this.timingFinished = co.aikar.timings.WorldTimingsHandler.getTickList(world, timingsType + " - Finish");
        this.currentTick = this.world.getGameTime();
    }

    private void queueEntryForTick(final NextTickListEntry<T> entry, final ServerChunkProvider chunkProvider) {
        if (entry.tickState == STATE_SCHEDULED) {
            if (chunkProvider.isTickingReadyMainThread(entry.pos)) {
                this.toTickThisTick.add(entry);
                entry.tickState = STATE_PENDING_TICK;
            } else {
                // we dump them to a map to avoid constantly re-scheduling them
                this.addToNotTickingReady(entry);
            }
        }
    }

    private void addToNotTickingReady(final NextTickListEntry<T> entry) {
        this.pendingChunkTickLoad.computeIfAbsent(MCUtil.getCoordinateKey(entry.pos), (long keyInMap) -> {
            return new ArrayList<>();
        }).add(entry);
    }

    private void addToSchedule(final NextTickListEntry<T> entry) {
        long delay = entry.triggerTick - (this.currentTick + 1);
        if (delay < SHORT_SCHEDULE_TICK_THRESHOLD) {
            if (delay < 0) {
                // longScheduled orders by tick time, short scheduled does not
                this.longScheduled.add(entry);
            } else {
                this.shortScheduled[getWrappedIndex(this.shortScheduledIndex, SHORT_SCHEDULE_TICK_THRESHOLD, (int) delay)].addEntryLast(entry);
            }
        } else {
            this.longScheduled.add(entry);
        }
    }

    private void removeEntry(final NextTickListEntry<T> entry) {
        entry.tickState = STATE_CANCELLED_TICK;
        // short/long scheduled will skip the entry

        final BlockPos pos = entry.pos;
        final long blockKey = MCUtil.getBlockKey(pos);

        final ArrayList<NextTickListEntry<T>> currentEntries = this.entriesByBlock.get(blockKey);

        if (currentEntries.size() == 1) {
            // it should contain our entry
            this.entriesByBlock.remove(blockKey);
        } else {
            // it's more likely that this entry is at the start of the list than the end
            for (int i = 0, len = currentEntries.size(); i < len; ++i) {
                final NextTickListEntry<T> currentEntry = currentEntries.get(i);
                if (currentEntry == entry) {
                    currentEntries.remove(i);
                    break;
                }
            }
        }

        final long chunkKey = MCUtil.getCoordinateKey(entry.pos);

        ObjectRBTreeSet<NextTickListEntry<T>> set = this.entriesByChunk.get(chunkKey);

        set.remove(entry);

        if (set.isEmpty()) {
            this.entriesByChunk.remove(chunkKey);
        }

        ArrayList<NextTickListEntry<T>> pendingTickingLoad = this.pendingChunkTickLoad.get(chunkKey);

        if (pendingTickingLoad != null) {
            for (int i = 0, len = pendingTickingLoad.size(); i < len; ++i) {
                if (pendingTickingLoad.get(i) == entry) {
                    pendingTickingLoad.remove(i);
                    break;
                }
            }

            if (pendingTickingLoad.isEmpty()) {
                this.pendingChunkTickLoad.remove(chunkKey);
            }
        }

        long delay = entry.triggerTick - (this.currentTick + 1);
        if (delay >= SHORT_SCHEDULE_TICK_THRESHOLD) {
            this.longScheduled.remove(entry);
        }
    }

    public void onChunkSetTicking(final int chunkX, final int chunkZ) {
        final ArrayList<NextTickListEntry<T>> pending = this.pendingChunkTickLoad.remove(MCUtil.getCoordinateKey(chunkX, chunkZ));
        if (pending == null) {
            return;
        }

        for (int i = 0, size = pending.size(); i < size; ++i) {
            final NextTickListEntry<T> entry = pending.get(i);
            // already in all the relevant reference maps, just need to add to longScheduled or shortScheduled
            this.addToSchedule(entry);
        }
    }

    private void prepare() {
        final long currentTick = this.currentTick;

        final ServerChunkProvider chunkProvider = this.world.getChunkSource();

        // here we setup what's going to tick

        // we don't remove items from shortScheduled (but do from longScheduled) because they're cleared at the end of
        // this tick
        if (this.longScheduled.isEmpty() || this.longScheduled.first().triggerTick > currentTick) {
            // nothing in longScheduled to worry about
            final TickListServerInterval<T> interval = this.shortScheduled[this.shortScheduledIndex];
            for (int i = 0, len = interval.byPriority.length; i < len; ++i) {
                for (final Iterator<NextTickListEntry<T>> iterator = interval.byPriority[i].iterator(); iterator.hasNext(); ) {
                    this.queueEntryForTick(iterator.next(), chunkProvider);
                }
            }
        } else {
            final TickListServerInterval<T> interval = this.shortScheduled[this.shortScheduledIndex];

            // combine interval and longScheduled, keeping order
            final Comparator<NextTickListEntry<T>> comparator = (Comparator) TickListServerInterval.ENTRY_COMPARATOR;
            final Iterator<NextTickListEntry<T>> longScheduledIterator = this.longScheduled.iterator();
            NextTickListEntry<T> longCurrent = longScheduledIterator.next();

            for (int i = 0, len = interval.byPriority.length; i < len; ++i) {
                for (final Iterator<NextTickListEntry<T>> iterator = interval.byPriority[i].iterator(); iterator.hasNext(); ) {
                    final NextTickListEntry<T> shortCurrent = iterator.next();
                    if (longCurrent != null) {
                        // drain longCurrent until we can add shortCurrent
                        while (comparator.compare(longCurrent, shortCurrent) <= 0) {
                            this.queueEntryForTick(longCurrent, chunkProvider);
                            longScheduledIterator.remove();
                            if (longScheduledIterator.hasNext()) {
                                longCurrent = longScheduledIterator.next();
                                if (longCurrent.triggerTick > currentTick) {
                                    longCurrent = null;
                                    break;
                                }
                            } else {
                                longCurrent = null;
                                break;
                            }
                        }
                    }
                    this.queueEntryForTick(shortCurrent, chunkProvider);
                }
            }

            // add remaining from long scheduled
            for (; ; ) {
                if (longCurrent == null || longCurrent.triggerTick > currentTick) {
                    break;
                }
                longScheduledIterator.remove();
                this.queueEntryForTick(longCurrent, chunkProvider);

                if (longScheduledIterator.hasNext()) {
                    longCurrent = longScheduledIterator.next();
                } else {
                    break;
                }
            }
        }
    }

    private boolean warnedAboutDesync;

    @Override
    public void nextTick() {
        ++this.currentTick;
        if (this.currentTick != this.world.getGameTime()) {
            if (!this.warnedAboutDesync) {
                this.warnedAboutDesync = true;
                MinecraftServer.LOGGER.error("World tick desync detected! Expected " + this.currentTick + " ticks, but got " + this.world.getGameTime() + " ticks for world '" + this.world.getWorld().getName() + "'", new Throwable());
                MinecraftServer.LOGGER.error("Preventing redstone from breaking by refusing to accept new tick time");
            }
        }
    }

    @Override
    public void tick() {
        final ServerChunkProvider chunkProvider = this.world.getChunkSource();

        this.world.getProfiler().push("cleaning");
        this.timingCleanup.startTiming();

        this.prepare();

        // this must be done here in case something schedules in the tick code
        this.shortScheduled[this.shortScheduledIndex].clear();
        this.shortScheduledIndex = getNextIndex(this.shortScheduledIndex, SHORT_SCHEDULE_TICK_THRESHOLD);

        this.timingCleanup.stopTiming();
        this.world.getProfiler().popPush("ticking");
        this.timingTicking.startTiming();

        for (final NextTickListEntry<T> toTick : this.toTickThisTick) {
            if (toTick.tickState != STATE_PENDING_TICK) {
                // onTickEnd gets called at end of tick
                continue;
            }
            try {
                if (chunkProvider.isTickingReadyMainThread(toTick.pos)) {
                    toTick.tickState = STATE_TICKING;
                    this.tickFunction.accept(toTick);
                    if (toTick.tickState == STATE_TICKING) {
                        toTick.tickState = STATE_TICKED;
                    } // else it's STATE_CANCELLED_TICK
                } else {
                    // re-schedule eventually
                    toTick.tickState = STATE_SCHEDULED;
                    this.addToNotTickingReady(toTick);
                }
            } catch (final Throwable thr) {
                // start copy from TickListServer // TODO check on update
                CrashReport crashreport = CrashReport.forThrowable(thr, "Exception while ticking");
                CrashReportCategory crashreportsystemdetails = crashreport.addCategory("Block being ticked");

                //CrashReportCategory.populateBlockDetails(crashreportsystemdetails, this.world, toTick.pos, (BlockState) null); // Mohist - method not found - comment
                CrashReportCategory.populateBlockDetails(crashreportsystemdetails, toTick.pos, (BlockState) null); // Mohist - method not found - comment
                throw new ReportedException(crashreport);
                // end copy from TickListServer
            }
        }

        this.timingTicking.stopTiming();
        this.world.getProfiler().pop();
        this.timingFinished.startTiming();

        // finished ticking, actual cleanup time
        for (int i = 0, len = this.toTickThisTick.size(); i < len; ++i) {
            final NextTickListEntry<T> entry = this.toTickThisTick.poll();
            if (entry.tickState != STATE_SCHEDULED) {
                // some entries get re-scheduled due to their chunk not being loaded/at correct status, so do not
                // call onTickEnd for them
                this.onTickEnd(entry);
            }
        }

        this.timingFinished.stopTiming();
    }

    private void onTickEnd(final NextTickListEntry<T> entry) {
        if (entry.tickState == STATE_CANCELLED_TICK) {
            return;
        }
        entry.tickState = STATE_UNSCHEDULED;

        final BlockPos pos = entry.pos;
        final long blockKey = MCUtil.getBlockKey(pos);

        final ArrayList<NextTickListEntry<T>> currentEntries = this.entriesByBlock.get(blockKey);

        if (currentEntries.size() == 1) {
            // it should contain our entry
            this.entriesByBlock.remove(blockKey);
        } else {
            // it's more likely that this entry is at the start of the list than the end
            for (int i = 0, len = currentEntries.size(); i < len; ++i) {
                final NextTickListEntry<T> currentEntry = currentEntries.get(i);
                if (currentEntry == entry) {
                    currentEntries.remove(i);
                    break;
                }
            }
        }

        final long chunkKey = MCUtil.getCoordinateKey(entry.pos);

        ObjectRBTreeSet<NextTickListEntry<T>> set = this.entriesByChunk.get(chunkKey);

        set.remove(entry);

        if (set.isEmpty()) {
            this.entriesByChunk.remove(chunkKey);
        }

        // already removed from longScheduled or shortScheduled
    }

    @Override
    public boolean willTickThisTick(final BlockPos blockposition, final T data) {
        final ArrayList<NextTickListEntry<T>> entries = this.entriesByBlock.get(MCUtil.getBlockKey(blockposition));

        if (entries == null) {
            return false;
        }

        for (int i = 0, size = entries.size(); i < size; ++i) {
            final NextTickListEntry<T> entry = entries.get(i);
            if (entry.getType() == data && entry.tickState == STATE_PENDING_TICK) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasScheduledTick(final BlockPos blockposition, final T data) {
        final ArrayList<NextTickListEntry<T>> entries = this.entriesByBlock.get(MCUtil.getBlockKey(blockposition));

        if (entries == null) {
            return false;
        }

        for (int i = 0, size = entries.size(); i < size; ++i) {
            final NextTickListEntry<T> entry = entries.get(i);
            if (entry.getType() == data && entry.tickState == STATE_SCHEDULED) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void scheduleTick(BlockPos blockPosition, T t, int i, TickPriority tickListPriority) {
        this.schedule(blockPosition, t, i + this.currentTick, tickListPriority);
    }

    public void schedule(final NextTickListEntry<T> entry) {
        this.schedule(entry.pos, entry.getType(), entry.triggerTick, entry.priority);
    }

    public void schedule(final BlockPos pos, final T data, final long targetTick, final TickPriority priority) {
        final NextTickListEntry<T> entry = new NextTickListEntry<>(pos, data, targetTick, priority);
        if (this.excludeFromScheduling.test(entry.getType())) {
            return;
        }

        if (WARN_ON_EXCESSIVE_DELAY) {
            final long delay = entry.triggerTick - this.currentTick;
            if (delay >= EXCESSIVE_DELAY_THRESHOLD) {
                MinecraftServer.LOGGER.warn("Entry " + entry.toString() + " has been scheduled with an excessive delay of: " + delay, new Throwable());
            }
        }

        final long blockKey = MCUtil.getBlockKey(pos);

        final ArrayList<NextTickListEntry<T>> currentEntries = this.entriesByBlock.computeIfAbsent(blockKey, (long keyInMap) -> new ArrayList<>(3));

        if (currentEntries.isEmpty()) {
            currentEntries.add(entry);
        } else {
            for (int i = 0, size = currentEntries.size(); i < size; ++i) {
                final NextTickListEntry<T> currentEntry = currentEntries.get(i);

                // entries are only blocked from scheduling if currentEntry.equals(toSchedule) && currentEntry is scheduled to tick (NOT including pending)
                if (currentEntry.getType() == entry.getType() && currentEntry.tickState == STATE_SCHEDULED) {
                    // can't add
                    return;
                }
            }
            currentEntries.add(entry);
        }

        entry.tickState = STATE_SCHEDULED;

        this.entriesByChunk.computeIfAbsent(MCUtil.getCoordinateKey(entry.pos), (final long keyInMap) -> {
            return new ObjectRBTreeSet<>(TickListServerInterval.ENTRY_COMPARATOR);
        }).add(entry);

        this.addToSchedule(entry);
    }

    public void scheduleAll(final Iterator<NextTickListEntry<T>> iterator) {
        while (iterator.hasNext()) {
            this.schedule(iterator.next());
        }
    }

    // this is not the standard interception calculation, but it's the one vanilla uses
    // i.e the y value is ignored? the x, z calc isn't correct?
    // however for the copy op they use the correct intersection, after using this one of course...
    private static boolean isBlockInSortof(final MutableBoundingBox boundingBox, final BlockPos pos) {
        return pos.getX() >= boundingBox.minX() && pos.getX() < boundingBox.maxX() && pos.getZ() >= boundingBox.minZ() && pos.getZ() < boundingBox.maxZ();
    }

    @Override
    public List<NextTickListEntry<T>> fetchTicksInArea(final MutableBoundingBox structureboundingbox, final boolean removeReturned, final boolean excludeTicked) {
        if (structureboundingbox.minX() == structureboundingbox.maxX() || structureboundingbox.minZ() == structureboundingbox.maxZ()) {
            return Collections.emptyList(); // vanilla behaviour, check isBlockInSortof above
        }

        final int lowerChunkX = structureboundingbox.minX() >> 4;
        final int upperChunkX = (structureboundingbox.maxX() - 1) >> 4; // subtract 1 since maxX is exclusive
        final int lowerChunkZ = structureboundingbox.minZ() >> 4;
        final int upperChunkZ = (structureboundingbox.maxZ() - 1) >> 4; // subtract 1 since maxZ is exclusive

        final int xChunksLength = (upperChunkX - lowerChunkX + 1);
        final int zChunksLength = (upperChunkZ - lowerChunkZ + 1);

        final ObjectRBTreeSet<NextTickListEntry<T>>[] containingChunks = new ObjectRBTreeSet[xChunksLength * zChunksLength];

        final int offset = (xChunksLength * -lowerChunkZ - lowerChunkX);
        int totalEntries = 0;
        for (int currChunkX = lowerChunkX; currChunkX <= upperChunkX; ++currChunkX) {
            for (int currChunkZ = lowerChunkZ; currChunkZ <= upperChunkZ; ++currChunkZ) {
                // todo optimize
                //final int index = (currChunkX - lowerChunkX) + xChunksLength * (currChunkZ - lowerChunkZ);
                final int index = offset + currChunkX + xChunksLength * currChunkZ;
                final ObjectRBTreeSet<NextTickListEntry<T>> set = containingChunks[index] = this.entriesByChunk.get(MCUtil.getCoordinateKey(currChunkX, currChunkZ));
                if (set != null) {
                    totalEntries += set.size();
                }
            }
        }

        final List<NextTickListEntry<T>> ret = new ArrayList<>(totalEntries);

        final int matchOne = (STATE_SCHEDULED | STATE_PENDING_TICK) | (excludeTicked ? 0 : (STATE_TICKING | STATE_TICKED));

        MCUtil.mergeSortedSets((NextTickListEntry<T> entry) -> {
            if (!isBlockInSortof(structureboundingbox, entry.pos)) {
                return;
            }
            final int tickState = entry.tickState;
            if ((tickState & matchOne) == 0) {
                return;
            }

            ret.add(entry);
            return;
        }, TickListServerInterval.ENTRY_COMPARATOR, containingChunks);

        if (removeReturned) {
            for (NextTickListEntry<T> entry : ret) {
                this.removeEntry(entry);
            }
        }

        return ret;
    }

    @Override
    public void copy(MutableBoundingBox structureboundingbox, BlockPos blockposition) {
        // start copy from TickListServer // TODO check on update
        List<NextTickListEntry<T>> list = this.fetchTicksInArea(structureboundingbox, false, false);
        Iterator<NextTickListEntry<T>> iterator = list.iterator();

        while (iterator.hasNext()) {
            NextTickListEntry<T> nextticklistentry = iterator.next();

            if (structureboundingbox.isInside(nextticklistentry.pos)) {
                BlockPos blockposition1 = nextticklistentry.pos.offset(blockposition);
                T t0 = nextticklistentry.getType();

                this.schedule(new NextTickListEntry<>(blockposition1, t0, nextticklistentry.triggerTick, nextticklistentry.priority));
            }
        }
        // end copy from TickListServer
    }

    @Override
    public List<NextTickListEntry<T>> fetchTicksInChunk(ChunkPos chunkPos, boolean removeReturned, boolean excludeTicked) {
        // Vanilla DOES get the entries 2 blocks out of the chunk too, but that doesn't matter since we ignore chunks
        // not at ticking status, and ticking status requires neighbours loaded
        // so with this method we will reduce scheduler churning
        final int matchOne = (STATE_SCHEDULED | STATE_PENDING_TICK) | (excludeTicked ? 0 : (STATE_TICKING | STATE_TICKED));

        final ObjectRBTreeSet<NextTickListEntry<T>> entries = this.entriesByChunk.get(MCUtil.getCoordinateKey(chunkPos));

        if (entries == null) {
            return Collections.emptyList();
        }

        final List<NextTickListEntry<T>> ret = new ArrayList<>(entries.size());

        for (NextTickListEntry<T> entry : entries) {
            if ((entry.tickState & matchOne) == 0) {
                continue;
            }
            ret.add(entry);
        }

        if (removeReturned) {
            for (NextTickListEntry<T> entry : ret) {
                this.removeEntry(entry);
            }
        }

        return ret;
    }

    @Override
    public ListNBT save(ChunkPos chunkcoordintpair) {
        // start copy from TickListServer  // TODO check on update
        List<NextTickListEntry<T>> list = this.fetchTicksInChunk(chunkcoordintpair, false, true);

        return ServerTickList.saveTickList(this.getMinecraftKeyFrom, list, this.currentTick);
        // end copy from TickListServer
    }

    @Override
    public int size() {
        // good thing this is only used in debug reports // TODO check on update
        int ret = 0;

        for (NextTickListEntry<T> entry : this.longScheduled) {
            if (entry.tickState == STATE_SCHEDULED) {
                ++ret;
            }
        }

        for (Iterator<Long2ObjectMap.Entry<ArrayList<NextTickListEntry<T>>>> iterator = this.pendingChunkTickLoad.long2ObjectEntrySet().iterator(); iterator.hasNext(); ) {
            ArrayList<NextTickListEntry<T>> list = iterator.next().getValue();

            for (NextTickListEntry<T> entry : list) {
                if (entry.tickState == STATE_SCHEDULED) {
                    ++ret;
                }
            }
        }

        for (TickListServerInterval<T> interval : this.shortScheduled) {
            for (Iterable<NextTickListEntry<T>> set : interval.byPriority) {
                for (NextTickListEntry<T> entry : set) {
                    if (entry.tickState == STATE_SCHEDULED) {
                        ++ret;
                    }
                }
            }
        }

        return ret;
    }
}

