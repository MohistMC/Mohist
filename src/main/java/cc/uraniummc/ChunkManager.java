package cc.uraniummc;

import cern.colt.function.IntObjectProcedure;
import cern.colt.function.LongObjectProcedure;
import cern.colt.function.ObjectProcedure;
import cern.colt.map.OpenIntObjectHashMap;
import cern.colt.map.OpenLongObjectHashMap;
import net.minecraft.world.chunk.Chunk;

public class ChunkManager {
    private static final int MIN = -32768, MAX = 32767;
    public OpenIntObjectHashMap loadedChunksLSB = new OpenIntObjectHashMap();
    public OpenLongObjectHashMap loadefChunksMSB = new OpenLongObjectHashMap();

    public ChunkManager() {
    }

    public Chunk getChunk(final int x, final int z) {
        if (MIN <= x && MIN <= z && x <= MAX && z <= MAX)
            return (Chunk) loadedChunksLSB.get(x << 16 | (z & 0xFFFF));
        else
            return (Chunk) loadefChunksMSB.get(((long) x) << 32 | z);
    }

    public boolean chunkExists(final int x, final int z) {
        if (MIN <= x && MIN <= z && x <= MAX && z <= MAX)
            return loadedChunksLSB.containsKey(x << 16 | (z & 0xFFFF));
        else
            return loadefChunksMSB.containsKey(((long) x) << 32 | z);
    }

    public void putChunk(Chunk chunk) {
        putChunk(chunk, chunk.xPosition, chunk.zPosition);
    }

    public void putChunk(Chunk chunk, final int x, final int z) {
        if (MIN <= x && MIN <= z && x <= MAX && z <= MAX)
            loadedChunksLSB.put(x << 16 | (z & 0xFFFF), chunk);
        else
            loadefChunksMSB.put(((long) x) << 32 | z, chunk);
    }

    public boolean removeKey(final int x, final int z) {
        if (MIN <= x && MIN <= z && x <= MAX && z <= MAX)
            return loadedChunksLSB.removeKey(x << 16 | (z & 0xFFFF));
        else
            return loadefChunksMSB.removeKey(((long) x) << 32 | z);
    }

    public Chunk remove(final int x, final int z) {
        Chunk chunk = getChunk(x, z);
        return removeKey(x, z) ? chunk : null;
    }

    public int size() {
        return loadedChunksLSB.size() + loadefChunksMSB.size();
    }

    public void forEach(final ObjectProcedure proc) {
        loadedChunksLSB.forEachPair(new IntObjectProcedure() {
            @Override
            public boolean apply(int arg0, Object arg1) {
                return proc.apply(arg1);
            }
        });
        loadefChunksMSB.forEachPair(new LongObjectProcedure() {
            @Override
            public boolean apply(long arg0, Object arg1) {
                return proc.apply(arg1);
            }
        });
    }
}
