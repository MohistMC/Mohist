package cc.uraniummc;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

public enum ChunkGenerator {
    INSTANCE;
    private final Queue<QueuedChunk> queue = Queues.newArrayDeque();
    private final LongHashMap map = new LongHashMap();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void queueChunkGeneration(ChunkProviderServer provider, int cx, int cz, ChunkCallback callback) {
        long key = ChunkCoordIntPair.chunkXZ2Int(cx, cz);
        QueuedChunk chunk;
        lock.readLock().lock();
        try {
            chunk = (QueuedChunk) map.getValueByKey(key);
        } finally {
            lock.readLock().unlock();
        }
        if (chunk != null) {
            chunk.callbacks.add(callback);
        } else {
            chunk = new QueuedChunk(provider, cx, cz, callback);
            lock.writeLock().lock();
            try {
                map.add(key, chunk);
                queue.add(chunk);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public void chunkGeneratorCycle() {
        int max = MinecraftServer.uraniumConfig.commonMaxChunkGenPerTick.getValue();
        lock.writeLock().lock();
        try {
            for (int i = 0; i < max && internalGenerate(); i++)
                ;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean generate() {
        lock.writeLock().lock();
        try {
            return internalGenerate();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean internalGenerate() {
        for (QueuedChunk chunk; (chunk = queue.poll()) != null;) {
            map.remove(ChunkCoordIntPair.chunkXZ2Int(chunk.cx, chunk.cz));
            if (chunk.provider.loadAsync(chunk.cx, chunk.cz, false, chunk))
                continue;
            chunk.onChunkLoaded(chunk.provider.originalLoadChunk(chunk.cx, chunk.cz));
            return true;
        }
        return false;
    }

    private static class QueuedChunk implements ChunkCallback {
        public final ChunkProviderServer provider;
        public final int cx;
        public final int cz;
        public final List<ChunkCallback> callbacks = Lists.newArrayListWithCapacity(1);

        public QueuedChunk(ChunkProviderServer provider, int cx, int cz, ChunkCallback callback) {
            this.provider = provider;
            this.cx = cx;
            this.cz = cz;
            this.callbacks.add(callback);
        }

        @Override
        public void onChunkLoaded(Chunk chunk) {
            for (ChunkCallback cb : callbacks)
                cb.onChunkLoaded(chunk);
        }
    }
}
