package org.bukkit.craftbukkit.generator;

import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.StructureSettings;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator extends net.minecraft.world.level.chunk.ChunkGenerator {

    public InternalChunkGenerator(BiomeSource worldchunkmanager, StructureSettings structuresettings) {
        super(worldchunkmanager, structuresettings);
    }
}
