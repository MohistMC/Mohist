
package carpet.helpers;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderServer;

import java.util.ArrayList;
import java.util.List;

public class RandomTickOptimization {

    public static MinecraftServer minecraft_server;
    private static List<Block> USELESS_RANDOMTICKS = new ArrayList<>();
    public static boolean needsWorldGenFix = false;

    static {
        for (Block b : Block.REGISTRY) {
            if (b instanceof BlockBasePressurePlate
                    || b instanceof BlockButton
                    || b instanceof BlockPumpkin
                    || b instanceof BlockRedstoneTorch) {
                USELESS_RANDOMTICKS.add(b);
            }
        }
        USELESS_RANDOMTICKS.add(Blocks.CAKE);
        USELESS_RANDOMTICKS.add(Blocks.CARPET);
        USELESS_RANDOMTICKS.add(Blocks.DETECTOR_RAIL);
        USELESS_RANDOMTICKS.add(Blocks.SNOW);
        USELESS_RANDOMTICKS.add(Blocks.TORCH);
        USELESS_RANDOMTICKS.add(Blocks.TRIPWIRE);
        USELESS_RANDOMTICKS.add(Blocks.TRIPWIRE_HOOK);
    }

    public static void setUselessRandomTicks(boolean on) {
        USELESS_RANDOMTICKS.forEach(b -> b.setTickRandomly(on));
    }

    public static void setLiquidRandomTicks(boolean on) {
        needsWorldGenFix = !on;
        Blocks.FLOWING_WATER.setTickRandomly(on);
        Blocks.FLOWING_LAVA.setTickRandomly(on);
    }

    public static void setSpongeRandomTicks(boolean on) {
        Blocks.SPONGE.setTickRandomly(on);
    }

    public static void recalculateAllChunks() {
        if (minecraft_server.worlds == null) // worlds not loaded yet
            return;
        for (World world : minecraft_server.worlds) {
            IChunkProvider provider = world.getChunkProvider();
            if (!(provider instanceof ChunkProviderServer))
                continue;
            for (Chunk chunk : ((ChunkProviderServer) provider).id2ChunkMap.values()) {
                for (ExtendedBlockStorage subchunk : chunk.getBlockStorageArray()) {
                    if (subchunk != null)
                        subchunk.recalculateRefCounts();
                }
            }
        }
    }
    public static void init(MinecraftServer server) {
        minecraft_server = server;
        setLiquidRandomTicks(true);
        recalculateAllChunks();
        setSpongeRandomTicks(false);
        recalculateAllChunks();
        setUselessRandomTicks(false);
        recalculateAllChunks();
    }
}