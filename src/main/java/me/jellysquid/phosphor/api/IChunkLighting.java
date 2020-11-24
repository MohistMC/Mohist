package me.jellysquid.phosphor.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

public interface IChunkLighting {
    int getCachedLightFor(EnumSkyBlock enumSkyBlock, BlockPos pos);
}