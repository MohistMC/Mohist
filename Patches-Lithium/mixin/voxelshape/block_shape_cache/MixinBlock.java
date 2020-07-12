package me.jellysquid.mods.lithium.mixin.voxelshape.block_shape_cache;

import me.jellysquid.mods.lithium.common.block.BlockStateWithShapeCache;
import me.jellysquid.mods.lithium.common.block.ExtendedBlockShapeCache;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Replaces a number of functions in the Block class which are used to determine if some redstone components and other
 * blocks can stand on top of another block. These functions make use of additional cached data in BlockState$ShapeCache.
 */
@Mixin(Block.class)
public class MixinBlock {
    @Shadow
    @Final
    private static VoxelShape field_220083_b;

    @Shadow
    @Final
    private static VoxelShape field_220084_c;

    /**
     * @reason Use the shape cache
     * @author JellySquid
     */
    @Overwrite
    public static boolean hasSolidSideOnTop(IBlockReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState.Cache shapeCache = ((BlockStateWithShapeCache) state).bridge$getShapeCache();

        if (shapeCache != null) {
            return ((ExtendedBlockShapeCache) shapeCache).hasSolidSide(Direction.UP);
        }

        return hasSolidSide(state, state.getCollisionShape(world, pos).project(Direction.UP), field_220083_b);
    }

    /**
     * @reason Use the shape cache
     * @author JellySquid
     */
    @Overwrite
    public static boolean hasEnoughSolidSide(IWorldReader world, BlockPos pos, Direction side) {
        BlockState state = world.getBlockState(pos);
        BlockState.Cache shapeCache = ((BlockStateWithShapeCache) state).bridge$getShapeCache();

        if (shapeCache != null) {
            return ((ExtendedBlockShapeCache) shapeCache).hasEnoughSolidSide(side);
        }

        return hasSolidSide(state, state.getCollisionShape(world, pos).project(side), field_220084_c);
    }

    private static boolean hasSolidSide(BlockState state, VoxelShape shape, VoxelShape square) {
        if (!state.isIn(BlockTags.LEAVES)) {
            // Avoid the expensive call to VoxelShapes#matchesAnywhere if the block in question is a full cube
            if (shape == VoxelShapes.fullCube()) {
                return true;
            }

            return !VoxelShapes.compare(shape, square, IBooleanFunction.ONLY_SECOND);
        }

        return false;
    }
}
