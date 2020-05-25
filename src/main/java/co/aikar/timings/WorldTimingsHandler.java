package co.aikar.timings;

import net.minecraft.world.World;

/**
 * Set of timers per world, to track world specific timings.
 */
public class WorldTimingsHandler {
    public final Timing mobSpawn;
    public final Timing doChunkUnload;
    public final Timing doPortalForcer;
    public final Timing scheduledBlocks;
    public final Timing scheduledBlocksCleanup;
    public final Timing scheduledBlocksTicking;
    public final Timing chunkTicks;
    public final Timing lightChunk;
    public final Timing chunkTicksBlocks;
    public final Timing doVillages;
    public final Timing doChunkMap;
    public final Timing doChunkMapUpdate;
    public final Timing doChunkMapToUpdate;
    public final Timing doChunkMapSortMissing;
    public final Timing doChunkMapSortSendToPlayers;
    public final Timing doChunkMapPlayersNeedingChunks;
    public final Timing doChunkMapPendingSendToPlayers;
    public final Timing doChunkMapUnloadChunks;
    public final Timing doChunkGC;
    public final Timing doSounds;
    public final Timing entityRemoval;
    public final Timing entityTick;
    public final Timing tileEntityTick;
    public final Timing tileEntityPending;
    public final Timing tracker1;
    public final Timing tracker2;
    public final Timing doTick;
    public final Timing tickEntities;

    public final Timing syncChunkLoadTimer;
    public final Timing syncChunkLoadDataTimer;
    public final Timing syncChunkLoadStructuresTimer;
    public final Timing syncChunkLoadPostTimer;
    public final Timing syncChunkLoadNBTTimer;
    public final Timing syncChunkLoadPopulateNeighbors;
    public final Timing chunkGeneration;
    public final Timing chunkIOStage1;
    public final Timing chunkIOStage2;
    public final Timing worldSave;
    public final Timing worldSaveChunks;
    public final Timing worldSaveLevel;
    public final Timing chunkSaveData;

    public final Timing lightingQueueTimer;

    public WorldTimingsHandler(World server) {
        String name = server.worldInfo.getWorldName() +" - ";

        mobSpawn = Timings.ofSafe(name + "mobSpawn");
        doChunkUnload = Timings.ofSafe(name + "doChunkUnload");
        scheduledBlocks = Timings.ofSafe(name + "Scheduled Blocks");
        scheduledBlocksCleanup = Timings.ofSafe(name + "Scheduled Blocks - Cleanup");
        scheduledBlocksTicking = Timings.ofSafe(name + "Scheduled Blocks - Ticking");
        chunkTicks = Timings.ofSafe(name + "Chunk Ticks");
        lightChunk = Timings.ofSafe(name + "Light Chunk");
        chunkTicksBlocks = Timings.ofSafe(name + "Chunk Ticks - Blocks");
        doVillages = Timings.ofSafe(name + "doVillages");
        doChunkMap = Timings.ofSafe(name + "doChunkMap");
        doChunkMapUpdate = Timings.ofSafe(name + "doChunkMap - Update");
        doChunkMapToUpdate = Timings.ofSafe(name + "doChunkMap - To Update");
        doChunkMapSortMissing = Timings.ofSafe(name + "doChunkMap - Sort Missing");
        doChunkMapSortSendToPlayers = Timings.ofSafe(name + "doChunkMap - Sort Send To Players");
        doChunkMapPlayersNeedingChunks = Timings.ofSafe(name + "doChunkMap - Players Needing Chunks");
        doChunkMapPendingSendToPlayers = Timings.ofSafe(name + "doChunkMap - Pending Send To Players");
        doChunkMapUnloadChunks = Timings.ofSafe(name + "doChunkMap - Unload Chunks");
        doSounds = Timings.ofSafe(name + "doSounds");
        doChunkGC = Timings.ofSafe(name + "doChunkGC");
        doPortalForcer = Timings.ofSafe(name + "doPortalForcer");
        entityTick = Timings.ofSafe(name + "entityTick");
        entityRemoval = Timings.ofSafe(name + "entityRemoval");
        tileEntityTick = Timings.ofSafe(name + "tileEntityTick");
        tileEntityPending = Timings.ofSafe(name + "tileEntityPending");

        syncChunkLoadTimer = Timings.ofSafe(name + "syncChunkLoad");
        syncChunkLoadDataTimer = Timings.ofSafe(name + "syncChunkLoad - Data");
        syncChunkLoadStructuresTimer = Timings.ofSafe(name + "chunkLoad - recreateStructures");
        syncChunkLoadPostTimer = Timings.ofSafe(name + "chunkLoad - Post");
        syncChunkLoadNBTTimer = Timings.ofSafe(name + "chunkLoad - NBT");
        syncChunkLoadPopulateNeighbors = Timings.ofSafe(name + "chunkLoad - Populate Neighbors");
        chunkGeneration = Timings.ofSafe(name + "chunkGeneration");
        chunkIOStage1 = Timings.ofSafe(name + "ChunkIO Stage 1 - DiskIO");
        chunkIOStage2 = Timings.ofSafe(name + "ChunkIO Stage 2 - Post Load");
        worldSave = Timings.ofSafe(name + "World Save");
        worldSaveLevel = Timings.ofSafe(name + "World Save - Level");
        worldSaveChunks = Timings.ofSafe(name + "World Save - Chunks");
        chunkSaveData = Timings.ofSafe(name + "Chunk Save - Data");

        tracker1 = Timings.ofSafe(name + "tracker stage 1");
        tracker2 = Timings.ofSafe(name + "tracker stage 2");
        doTick = Timings.ofSafe(name + "doTick");
        tickEntities = Timings.ofSafe(name + "tickEntities");

        lightingQueueTimer = Timings.ofSafe(name + "Lighting Queue");
    }
}
