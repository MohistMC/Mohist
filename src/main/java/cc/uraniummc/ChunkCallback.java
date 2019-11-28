package cc.uraniummc;

import net.minecraft.world.chunk.Chunk;

public interface ChunkCallback {
    void onChunkLoaded(Chunk chunk);
}
