package com.mohistmc.api;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunkData = this.createChunkData(world);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                biome.setBiome(x + i, z + j, Biome.THE_VOID);
            }
        }

        for (int y = 0; y < world.getMaxHeight(); y++) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    chunkData.setBlock(i, y, j, Material.AIR);
                }
            }
        }

        return chunkData;
    }
}
