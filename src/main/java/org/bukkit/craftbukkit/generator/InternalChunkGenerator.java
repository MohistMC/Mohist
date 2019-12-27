package org.bukkit.craftbukkit.generator;

import net.minecraft.world.IWorld;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.biome.provider.BiomeProvider;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator<C extends GeneratorSettingsDefault> extends net.minecraft.world.chunk.ChunkGenerator<C> {

    public InternalChunkGenerator(GeneratorAccess generatorAccess, WorldChunkManager worldChunkManager, C c0) {
        super(generatorAccess, worldChunkManager, c0);
    }
}
