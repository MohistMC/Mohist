package me.jellysquid.mods.lithium.common.block;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public interface ExtendedBlockShapeCache {
    /**
     * Cached version of {@link net.minecraft.block.Block#hasEnoughSolidSide(IWorldReader, BlockPos, Direction)}
     */
    boolean hasEnoughSolidSide(Direction facing);

    /**
     * Cached and directional version of {@link net.minecraft.block.Block#hasSolidSideOnTop(IBlockReader, BlockPos)}
     */
    boolean hasSolidSide(Direction facing);

    /**
     * Cached version of {@link net.minecraft.block.Block#isFaceFullSquare(net.minecraft.util.math.shapes.VoxelShape, Direction)}
     */
    boolean isFaceFullSquare(Direction facing);
}
