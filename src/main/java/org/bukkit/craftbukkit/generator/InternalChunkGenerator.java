package org.bukkit.craftbukkit.generator;

import net.minecraft.world.IWorld;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.biome.provider.BiomeProvider;

// Do not implement functions to this class, add to NormalChunkGenerator
public abstract class InternalChunkGenerator<C extends GenerationSettings> extends net.minecraft.world.gen.ChunkGenerator<C> {

    public InternalChunkGenerator(IWorld generatorAccess, BiomeProvider worldChunkManager, C c0) {
        super(generatorAccess, worldChunkManager, c0);
    }
}
