package com.destroystokyo.paper.world;


import co.aikar.timings.Timing;
import com.destroystokyo.paper.MCUtil;
import com.destroystokyo.paper.PaperMCConfig;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayDeque;

public class PaperLightingQueue {
    private static final long MAX_TIME = (long) (1000000000 / 20 * 1.15);

    public static void processQueue(long curTime) {
        final long startTime = System.nanoTime();
        final long maxTickTime = MAX_TIME - (startTime - curTime);

        if (maxTickTime <= 0) {
            return;
        }

        START:
        for (World world : MinecraftServer.getServerInst().worlds) {
            if (!PaperMCConfig.queueLightUpdates) {
                continue;
            }

            ObjectCollection<Chunk> loadedChunks = ((WorldServer) world).getChunkProvider().id2ChunkMap.values();
            for (Chunk chunk : loadedChunks.toArray(new Chunk[0])) {
                if (chunk.lightingQueue.processQueue(startTime, maxTickTime)) {
                    break START;
                }
            }
        }
    }

    public static class LightingQueue extends ArrayDeque<Runnable> {
        final private Chunk chunk;

        public LightingQueue(Chunk chunk) {
            super();
            this.chunk = chunk;
        }

        /**
         * Processes the lighting queue for this chunk
         *
         * @param startTime   If start Time is 0, we will not limit execution time
         * @param maxTickTime Maximum time to spend processing lighting updates
         * @return true to abort processing furthur lighting updates
         */
        public boolean processQueue(long startTime, long maxTickTime) {
            if (this.isEmpty()) {
                return false;
            }
            if (isOutOfTime(maxTickTime, startTime)) {

            }
            try (Timing ignored = chunk.world.timings.lightingQueueTimer.startTiming()) {
                Runnable lightUpdate;
                while ((lightUpdate = this.poll()) != null) {
                    lightUpdate.run();
                    if (isOutOfTime(maxTickTime, startTime)) {
                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * Flushes lighting updates to unload the chunk
         */
        public void processUnload() {
            if (!PaperMCConfig.queueLightUpdates) {
                return;
            }
            processQueue(0, 0); // No timeout

            final int radius = 1;
            for (int x = chunk.x - radius; x <= chunk.x + radius; ++x) {
                for (int z = chunk.z - radius; z <= chunk.z + radius; ++z) {
                    if (x == chunk.x && z == chunk.z) {
                        continue;
                    }

                    Chunk neighbor = MCUtil.getLoadedChunkWithoutMarkingActive(chunk.world, x, z);
                    if (neighbor != null) {
                        neighbor.lightingQueue.processQueue(0, 0); // No timeout
                    }
                }
            }
        }

        private static boolean isOutOfTime(long maxTickTime, long startTime) {
            return startTime > 0 && System.nanoTime() - startTime > maxTickTime;
        }
    }
}
