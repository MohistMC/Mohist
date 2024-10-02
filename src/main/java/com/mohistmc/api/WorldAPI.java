package com.mohistmc.api;

import com.mohistmc.plugins.world.WorldDate;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import java.util.Locale;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:49:40
 */
public class WorldAPI {

    public static String getDate(World world, WorldDate worldDate) {
        if (ConfigByWorlds.config.getString("worlds." + world.getName() + "." + worldDate.name().toLowerCase(Locale.ENGLISH)) == null) {
            switch (worldDate) {
                case NAME -> {
                    return world.getName();
                }
                case INFO -> {
                    return "-/-";
                }
                default -> throw new IllegalStateException("Unexpected value: " + worldDate);
            }
        }
        return ConfigByWorlds.config.getString("worlds." + world.getName() + "." + worldDate.name().toLowerCase(Locale.ENGLISH));
    }
	
    public static class VoidGenerator extends ChunkGenerator {
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
}
