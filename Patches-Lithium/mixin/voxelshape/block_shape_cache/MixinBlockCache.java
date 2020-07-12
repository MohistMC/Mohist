package me.jellysquid.mods.lithium.mixin.voxelshape.block_shape_cache;

import me.jellysquid.mods.lithium.common.block.ExtendedBlockShapeCache;
import me.jellysquid.mods.lithium.common.util.BitUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.EmptyBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockState.Cache.class)
public class MixinBlockCache implements ExtendedBlockShapeCache {
    private byte hasSolidSide;
    private byte hasEnoughSolidSide;

    private byte sideSolidFullSquare;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(BlockState state, CallbackInfo ci) {
        VoxelShape shape = state.getCollisionShape(EmptyBlockReader.INSTANCE, BlockPos.ZERO);

        for (Direction dir : DIRECTIONS) {
            if (!state.isIn(BlockTags.LEAVES)) {
                VoxelShape face = shape.project(dir);

                this.hasSolidSide |= BitUtil.bit(dir.ordinal(), calculateSideCoversSquare(face, field_220084_c));
                this.hasEnoughSolidSide |= BitUtil.bit(dir.ordinal(), calculateSideCoversSquare(face, field_220083_b));
                this.sideSolidFullSquare |= BitUtil.bit(dir.ordinal(), calculateIsFaceFullSquare(face));
            }
        }
    }

    @Override
    public boolean hasEnoughSolidSide(Direction facing) {
        return BitUtil.contains(this.hasSolidSide, facing.ordinal());
    }

    @Override
    public boolean hasSolidSide(Direction facing) {
        return BitUtil.contains(this.hasEnoughSolidSide, facing.ordinal());
    }

    @Override
    public boolean isFaceFullSquare(Direction facing) {
        return BitUtil.contains(this.sideSolidFullSquare, facing.ordinal());
    }

    private static boolean calculateSideCoversSquare(VoxelShape shape, VoxelShape square) {
        return shape == VoxelShapes.fullCube() || !VoxelShapes.compare(shape, square, IBooleanFunction.ONLY_SECOND);
    }

    private static boolean calculateIsFaceFullSquare(VoxelShape shape) {
        return shape == VoxelShapes.fullCube() || !VoxelShapes.compare(VoxelShapes.fullCube(), shape, IBooleanFunction.NOT_SAME);
    }

    private static final Direction[] DIRECTIONS = Direction.values();

    private static final VoxelShape field_220083_b = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D), IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape field_220084_c = Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 10.0D, 9.0D);
}
