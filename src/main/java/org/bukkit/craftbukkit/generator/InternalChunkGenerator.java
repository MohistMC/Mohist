package org.bukkit.craftbukkit.generator;

import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator extends ChunkGenerator {

    public InternalChunkGenerator(BiomeProvider worldchunkmanager, DimensionStructuresSettings structuresettings) {
        super(worldchunkmanager, structuresettings);
    }
}
