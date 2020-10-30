package net.minecraftforge.cauldron;

import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.google.gson.stream.JsonWriter;

public class CauldronHooks
{
    // Some mods such as Twilight Forest listen for specific events as their WorldProvider loads to hotload its dimension. This prevents this from happening so MV can create worlds using the same provider without issue.
    public static boolean craftWorldLoading = false;
    public static int tickingDimension = 0;
    public static ChunkCoordIntPair tickingChunk = null;
    public static Map<Class<? extends TileEntity>, TileEntityCache> tileEntityCache = new HashMap<Class<? extends TileEntity>, TileEntityCache>();
    public static Map<Class<? extends Entity>, SushchestvoCache> sushchestvoCache = new HashMap<Class<? extends Entity>, SushchestvoCache>();

    private static TObjectLongHashMap<CollisionWarning> recentWarnings = new TObjectLongHashMap<CollisionWarning>();

    public static void logInfo(String msg, Object... args)
    {
        MinecraftServer.getServer().logInfo(MessageFormat.format(msg, args));
    }

    public static void logWarning(String msg, Object... args)
    {
        MinecraftServer.getServer().logWarning(MessageFormat.format(msg, args));
    }

    public static void logSevere(String msg, Object... args)
    {
        MinecraftServer.getServer().logSevere(MessageFormat.format(msg, args));
    }

    public static void logStack()
    {
        if (MinecraftServer.cauldronConfig.logWithStackTraces.getValue())
        {
            Throwable ex = new Throwable();
            ex.fillInStackTrace();
            ex.printStackTrace();
        }
    }

    public static void logEntityDeath(Entity entity)
    {
        if (MinecraftServer.cauldronConfig.entityDeathLogging.getValue())
        {
            logInfo("Dim: {0} setDead(): {1}", entity.worldObj.provider.dimensionId, entity);
            logStack();
        }
    }

    public static void logEntityDespawn(Entity entity, String reason)
    {
        if (MinecraftServer.cauldronConfig.entityDespawnLogging.getValue())
        {
            logInfo("Dim: {0} Despawning ({1}): {2}", entity.worldObj.provider.dimensionId, reason, entity);
            //logInfo("Chunk Is Active: {0}", entity.worldObj.inActiveChunk(entity));
            logStack();
        }
    }

    public static void logEntitySpawn(World world, Entity entity, SpawnReason spawnReason)
    {
        if (MinecraftServer.cauldronConfig.entitySpawnLogging.getValue())
        {
            logInfo("Dim: {0} Spawning ({1}): {2}", world.provider.dimensionId, spawnReason, entity);
            logInfo("Dim: {0} Entities Last Tick: {1}", world.provider.dimensionId, world.entitiesTicked);
            logInfo("Dim: {0} Tiles Last Tick: {1}", world.provider.dimensionId, world.tilesTicked);
            //logInfo("Chunk Is Active: {0}", world.inActiveChunk(entity));
            logStack();
        }
    }

    public static void logChunkLoad(ChunkProviderServer provider, String msg, int x, int z, boolean logLoadOnRequest)
    {
        if (MinecraftServer.cauldronConfig.chunkLoadLogging.getValue())
        {
            logInfo("{0} Chunk At [{1}] ({2}, {3})", msg, provider.worldObj.provider.dimensionId, x, z);
            if (logLoadOnRequest)
            {
                logLoadOnRequest(provider, x, z);
            }
            logStack();
        }
    }

    public static void logChunkUnload(ChunkProviderServer provider, int x, int z, String msg)
    {
        if (MinecraftServer.cauldronConfig.chunkUnloadLogging.getValue())
        {
            logInfo("{0} [{1}] ({2}, {3})", msg, provider.worldObj.provider.dimensionId, x, z);
            long currentTick = MinecraftServer.getServer().getTickCounter();
            long lastAccessed = provider.lastAccessed(x, z);
            long diff = currentTick - lastAccessed;
            logInfo(" Last accessed: {0, number} Current Tick: {1, number} [{2, number}]", lastAccessed, currentTick, diff);
        }
    }

    private static void logLoadOnRequest(ChunkProviderServer provider, int x, int z)
    {
        long currentTick = MinecraftServer.getServer().getTickCounter();
        long lastAccessed = provider.lastAccessed(x, z);
        long diff = currentTick - lastAccessed;
        logInfo(" Last accessed: {0, number} Current Tick: {1, number} [{2, number}]", lastAccessed, currentTick, diff);
        logInfo(" Finding Spawn Point: {0}", provider.worldObj.findingSpawnPoint);
        logInfo(" Load chunk on request: {0}", provider.loadChunkOnProvideRequest);
        logInfo(" Calling Forge Tick: {0}", MinecraftServer.callingForgeTick);
        logInfo(" Load chunk on forge tick: {0}", MinecraftServer.cauldronConfig.loadChunkOnForgeTick.getValue());
        long providerTickDiff = currentTick - provider.initialTick;
        if (providerTickDiff <= 100)
        {
            logInfo(" Current Tick - Initial Tick: {0, number}", providerTickDiff);
        }
    }

    public static boolean checkBoundingBoxSize(Entity entity, AxisAlignedBB aabb)
    {
        if (entity instanceof EntityLivingBase && (!(entity instanceof IBossDisplayData) || !(entity instanceof IEntityMultiPart))
                && !(entity instanceof EntityPlayer))
        {
            int logSize = MinecraftServer.cauldronConfig.largeBoundingBoxLogSize.getValue();
            if (logSize <= 0 || !MinecraftServer.cauldronConfig.checkEntityBoundingBoxes.getValue()) return false;
            int x = MathHelper.floor_double(aabb.minX);
            int x1 = MathHelper.floor_double(aabb.maxX + 1.0D);
            int y = MathHelper.floor_double(aabb.minY);
            int y1 = MathHelper.floor_double(aabb.maxY + 1.0D);
            int z = MathHelper.floor_double(aabb.minZ);
            int z1 = MathHelper.floor_double(aabb.maxZ + 1.0D);

            int size = Math.abs(x1 - x) * Math.abs(y1 - y) * Math.abs(z1 - z);
            if (size > MinecraftServer.cauldronConfig.largeBoundingBoxLogSize.getValue())
            {
                logWarning("Entity being removed for bounding box restrictions");
                logWarning("BB Size: {0} > {1} avg edge: {2}", size, logSize, aabb.getAverageEdgeLength());
                logWarning("Motion: ({0}, {1}, {2})", entity.motionX, entity.motionY, entity.motionZ);
                logWarning("Calculated bounding box: {0}", aabb);
                logWarning("Entity bounding box: {0}", entity.getBoundingBox());
                logWarning("Entity: {0}", entity);
                NBTTagCompound tag = new NBTTagCompound();
                entity.writeToNBT(tag);
                logWarning("Entity NBT: {0}", tag);
                logStack();
                entity.setDead();
                return true;
            }
        }

        return false;
    }
    
    public static boolean checkEntitySpeed(Entity entity, double x, double y, double z)
    {
        int maxSpeed = MinecraftServer.cauldronConfig.entityMaxSpeed.getValue();
        if (maxSpeed > 0 && MinecraftServer.cauldronConfig.checkEntityMaxSpeeds.getValue())
        {
            double distance = x * x + z * z;
            if (distance > maxSpeed)
            {
                if (MinecraftServer.cauldronConfig.logEntitySpeedRemoval.getValue())
                {
                    logInfo("Speed violation: {0} was over {1} - Removing Entity: {2}", distance, maxSpeed, entity);
                    if (entity instanceof EntityLivingBase)
                    {
                        EntityLivingBase livingBase = (EntityLivingBase)entity;
                        logInfo("Entity Motion: ({0}, {1}, {2}) Move Strafing: {3} Move Forward: {4}", entity.motionX, entity.motionY, entity.motionZ, livingBase.moveStrafing, livingBase.moveForward);
                    }

                    if (MinecraftServer.cauldronConfig.logWithStackTraces.getValue())
                    {
                        logInfo("Move offset: ({0}, {1}, {2})", x, y, z);
                        logInfo("Motion: ({0}, {1}, {2})", entity.motionX, entity.motionY, entity.motionZ);
                        logInfo("Entity: {0}", entity);
                        NBTTagCompound tag = new NBTTagCompound();
                        entity.writeToNBT(tag);
                        logInfo("Entity NBT: {0}", tag);
                        logStack();
                    }
                }
                if (entity instanceof EntityPlayer) // Skip killing players
                {
                    entity.motionX = 0;
                    entity.motionY = 0;
                    entity.motionZ = 0;
                    return false;
                }
                // Remove the entity;
                entity.isDead = true;
                return false;
            }
        }
        return true;
    }
    
    public static void logEntitySize(World world, Entity entity, List list)
    {
        if (!MinecraftServer.cauldronConfig.logEntityCollisionChecks.getValue()) return;
        long largeCountLogSize = MinecraftServer.cauldronConfig.largeCollisionLogSize.getValue();
        if (largeCountLogSize > 0 && world.entitiesTicked > largeCountLogSize)
        {
            logWarning("Entity size > {0, number} at: {1}", largeCountLogSize, entity);
        }
        if (list == null) return;
        long largeCollisionLogSize = MinecraftServer.cauldronConfig.largeCollisionLogSize.getValue();
        if (largeCollisionLogSize > 0 &&
                (MinecraftServer.getServer().getTickCounter() % 10) == 0 &&
                list.size() >= largeCollisionLogSize)
        {
            CauldronHooks.CollisionWarning warning = new CauldronHooks.CollisionWarning(world, entity);
            if (recentWarnings.contains(warning))
            {
                long lastWarned = recentWarnings.get(warning);
                if ((MinecraftServer.getSystemTimeMillis() - lastWarned) < 30000) return;
            }
            recentWarnings.put(warning, System.currentTimeMillis());
            logWarning("Entity collision > {0, number} at: {1}", largeCollisionLogSize, entity);
        }
    }

    private static class CollisionWarning
    {
        public ChunkCoordinates chunkCoords;
        public int dimensionId;

        public CollisionWarning(World world, Entity entity)
        {
            this.dimensionId = world.provider.dimensionId;
            this.chunkCoords = new ChunkCoordinates(entity.chunkCoordX, entity.chunkCoordY, entity.chunkCoordZ);
        }

        @Override
        public boolean equals(Object otherObj)
        {
            if (!(otherObj instanceof CollisionWarning) || (otherObj == null)) return false;
            CollisionWarning other = (CollisionWarning) otherObj;
            return (other.dimensionId == this.dimensionId) && other.chunkCoords.equals(this.chunkCoords);
        }

        @Override
        public int hashCode()
        {
            return chunkCoords.hashCode() + dimensionId;
        }
    }

    /*
     * Thermos
     * 0 = false, dependent on others
     * 1 = true
     * -1 = false, absolute
     */
    public static byte canSushchestvoTick(Entity entity, World world)
    {
        if (entity == null || world.sushchestvoConfig == null) { return 0; }
        if (!MinecraftServer.sushchestvoConfig.skipEntityTicks.getValue()) { return 1; }
        
        int cX = net.minecraft.util.MathHelper.floor_double( entity.posX ) >> 4, cZ = net.minecraft.util.MathHelper.floor_double( entity.posZ ) >> 4;
        int iX = net.minecraft.util.MathHelper.floor_double( entity.posX ), iZ = net.minecraft.util.MathHelper.floor_double( entity.posZ );
        	SushchestvoCache seCache = sushchestvoCache.get(entity.getClass());
            if (seCache == null)
            {
                String seConfigPath = entity.getClass().getName().replace(".", "-");
                seConfigPath = seConfigPath.replaceAll("[^A-Za-z0-9\\-]", ""); // Fix up odd class names to prevent YAML errors
                seCache = new SushchestvoCache(entity.getClass(), world.getWorldInfo().getWorldName().toLowerCase(), seConfigPath, world.sushchestvoConfig.getBoolean(seConfigPath + ".tick-no-players", false), world.sushchestvoConfig.getBoolean(seConfigPath + ".never-ever-tick", false), world.sushchestvoConfig.getInt(seConfigPath + ".tick-interval", 1));
                sushchestvoCache.put(entity.getClass(), seCache);
            }

            if(seCache.neverEverTick)
            {
            	return -1;
            }
    
            // Skip tick interval
            if (seCache.tickInterval > 0 && (world.getWorldInfo().getWorldTotalTime() % seCache.tickInterval == 0L))
            {
                return 0;
            }

            // Tick with no players near?
            if (seCache.tickNoPlayers)
            {
                return 1;
            }

        	if(world.chunkProvider instanceof ChunkProviderServer) // Thermos - allow the server to tick entities that are in chunks trying to unload
        	{
        		ChunkProviderServer cps = ((ChunkProviderServer)world.chunkProvider);
        		if(cps.chunksToUnload.contains(cX, cZ))
        		{
        			Chunk c = cps.getChunkIfLoaded(cX, cZ);
        			if(c != null)
        			{
        				if(c.lastAccessedTick < 2L)
        				{
        					return 1;
        				}
        			}
        		}
        	}
        	
            return -1;
    }
    
    public static boolean canTileEntityTick(TileEntity tileEntity, World world)
    {
        if (tileEntity == null || world.tileentityConfig == null) return false;
        if (MinecraftServer.tileEntityConfig.skipTileEntityTicks.getValue())
        {
            TileEntityCache teCache = tileEntityCache.get(tileEntity.getClass());
            if (teCache == null)
            {
                String teConfigPath = tileEntity.getClass().getName().replace(".", "-");
                teConfigPath = teConfigPath.replaceAll("[^A-Za-z0-9\\-]", ""); // Fix up odd class names to prevent YAML errors
                teCache = new TileEntityCache(tileEntity.getClass(), world.getWorldInfo().getWorldName().toLowerCase(), teConfigPath, world.tileentityConfig.getBoolean(teConfigPath + ".tick-no-players", false), world.tileentityConfig.getInt(teConfigPath + ".tick-interval", 1));
                tileEntityCache.put(tileEntity.getClass(), teCache);
            }

            // Tick with no players near?
            if (!teCache.tickNoPlayers && !world.isActiveBlockCoord(tileEntity.xCoord, tileEntity.zCoord))
            {
                return false;
            }
    
            // Skip tick interval
            if (teCache.tickInterval > 0 && (world.getWorldInfo().getWorldTotalTime() % teCache.tickInterval == 0L))
            {
                return true;
            }
            
        	if(world.chunkProvider instanceof ChunkProviderServer) // Thermos - allow the server to tick tiles that are trying to unload
        	{
        		ChunkProviderServer cps = ((ChunkProviderServer)world.chunkProvider);
        		if(cps.chunksToUnload.contains(tileEntity.xCoord >> 4, tileEntity.zCoord >> 4))
        		{
        			Chunk c = cps.getChunkIfLoaded(tileEntity.xCoord >> 4, tileEntity.zCoord >> 4);
        			if(c != null)
        			{
        				if(c.lastAccessedTick < 2L)
        				{
        					return true;
        				}
        			}
        		}
        	}
        	
            return false;
        }
        return true;
    }

    public static boolean canUpdate(TileEntity tileEntity)
    {
        if (tileEntity == null || !tileEntity.canUpdate() || MinecraftServer.bannedTileEntityUpdates.contains(tileEntity.getClass())) return false; // quick exit
        return true;
    }

    public static void writeChunks(File file, boolean logAll)
    {
        try
        {
            if (file.getParentFile() != null)
            {
                file.getParentFile().mkdirs();
            }

            FileWriter fileWriter = new FileWriter(file);
            JsonWriter writer = new JsonWriter(fileWriter);
            writer.setIndent("  ");
            writer.beginArray();

            for (net.minecraft.world.WorldServer world : MinecraftServer.getServer().worlds)
            {
                writer.beginObject();
                writer.name("name").value(world.getWorld().getName());
                writer.name("dimensionId").value(world.provider.dimensionId);
                writer.name("players").value(world.playerEntities.size());
                writer.name("loadedChunks").value(world.theChunkProviderServer.loadedChunkHashMap_KC.size());
                writer.name("activeChunks").value(world.activeChunkSet.size());
                writer.name("entities").value(world.loadedEntityList.size());
                writer.name("tiles").value(world.loadedTileEntityList.size());

                TObjectIntHashMap<ChunkCoordIntPair> chunkEntityCounts = new TObjectIntHashMap<ChunkCoordIntPair>();
                TObjectIntHashMap<Class> classEntityCounts = new TObjectIntHashMap<Class>();
                TObjectIntHashMap<Entity> entityCollisionCounts = new TObjectIntHashMap<Entity>();
                Set<ChunkCoordinates> collidingCoords = new HashSet<ChunkCoordinates>();
                for (int i = 0; i < world.loadedEntityList.size(); i++)
                {
                    Entity entity = (Entity) world.loadedEntityList.get(i);
                    ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair((int) entity.posX >> 4, (int) entity.posZ >> 4);
                    chunkEntityCounts.adjustOrPutValue(chunkCoords, 1, 1);
                    classEntityCounts.adjustOrPutValue(entity.getClass(), 1, 1);
                    if ((entity.boundingBox != null) && logAll)
                    {
                        ChunkCoordinates coords = new ChunkCoordinates((int)Math.floor(entity.posX), (int)Math.floor(entity.posY), (int)Math.floor(entity.posZ));
                        if (!collidingCoords.contains(coords))
                        {
                            collidingCoords.add(coords);
                            int size = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.expand(1, 1, 1)).size();
                            if (size < 5)
                            {
                                continue;
                            }
                            entityCollisionCounts.put(entity, size);
                        }
                    }
                }

                TObjectIntHashMap<ChunkCoordIntPair> chunkTileCounts = new TObjectIntHashMap<ChunkCoordIntPair>();
                TObjectIntHashMap<Class> classTileCounts = new TObjectIntHashMap<Class>();
                writer.name("tiles").beginArray();
                for (int i = 0; i < world.loadedTileEntityList.size(); i++)
                {
                    TileEntity tile = (TileEntity) world.loadedTileEntityList.get(i);
                    if (logAll)
                    {
                        writer.beginObject();
                        writer.name("type").value(tile.getClass().toString());
                        writer.name("x").value(tile.xCoord);
                        writer.name("y").value(tile.yCoord);
                        writer.name("z").value(tile.zCoord);
                        writer.name("isInvalid").value(tile.isInvalid());
                        writer.name("canUpdate").value(tile.canUpdate());
                        writer.name("block").value("" + tile.getBlockType());
                        writer.endObject();
                    }
                    ChunkCoordIntPair chunkCoords = new ChunkCoordIntPair(tile.xCoord >> 4, tile.zCoord >> 4);
                    chunkTileCounts.adjustOrPutValue(chunkCoords, 1, 1);
                    classTileCounts.adjustOrPutValue(tile.getClass(), 1, 1);
                }
                writer.endArray();

                if (logAll)
                {
                    writeChunkCounts(writer, "topEntityColliders", entityCollisionCounts, 20);
                }
                writeChunkCounts(writer, "entitiesByClass", classEntityCounts);
                writeChunkCounts(writer, "entitiesByChunk", chunkEntityCounts);

                writeChunkCounts(writer, "tilesByClass", classTileCounts);
                writeChunkCounts(writer, "tilesByChunk", chunkTileCounts);

                writer.endObject(); // Dimension
            }
            writer.endArray(); // Dimensions
            writer.close();
            fileWriter.close();
        }
        catch (Throwable throwable)
        {
            MinecraftServer.getServer().logSevere("Could not save chunk info report to " + file);
        }
    }

    private static <T> void writeChunkCounts(JsonWriter writer, String name, final TObjectIntHashMap<T> map) throws IOException
    {
        writeChunkCounts(writer, name, map, 0);
    }

    private static <T> void writeChunkCounts(JsonWriter writer, String name, final TObjectIntHashMap<T> map, int max) throws IOException
    {
        List<T> sortedCoords = new ArrayList<T>(map.keySet());
        Collections.sort(sortedCoords, new Comparator<T>()
        {
            @Override
            public int compare(T s1, T s2)
            {
                return map.get(s2) - map.get(s1);
            }
        });

        int i = 0;
        writer.name(name).beginArray();
        for (T key : sortedCoords)
        {
            if ((max > 0) && (i++ > max))
            {
                break;
            }
            if (map.get(key) < 5)
            {
                continue;
            }
            writer.beginObject();
            writer.name("key").value(key.toString());
            writer.name("count").value(map.get(key));
            writer.endObject();
        }
        writer.endArray();
    }

    public static void dumpHeap(File file, boolean live)
    {
        try
        {
            if (file.getParentFile() != null)
            {
                file.getParentFile().mkdirs();
            }
            Class clazz = Class.forName("com.sun.management.HotSpotDiagnosticMXBean");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            Object hotspotMBean = ManagementFactory.newPlatformMXBeanProxy(server, "com.sun.management:type=HotSpotDiagnostic", clazz);
            Method m = clazz.getMethod("dumpHeap", String.class, boolean.class);
            m.invoke(hotspotMBean, file.getPath(), live);
        }
        catch (Throwable t)
        {
            logSevere("Could not write heap to {0}", file);
        }
    }

    public static void enableThreadContentionMonitoring()
    {
        if (!MinecraftServer.cauldronConfig.enableThreadContentionMonitoring.getValue()) return;
        java.lang.management.ThreadMXBean mbean = java.lang.management.ManagementFactory.getThreadMXBean();
        mbean.setThreadContentionMonitoringEnabled(true);
    }
}
