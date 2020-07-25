package io.izzel.arclight.common.mixin.v1_15.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.state.IProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.craftbukkit.v.event.CraftEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(ScaffoldingBlock.class)
public class ScaffoldingBlockMixin_1_15 {

    @Redirect(method = "tick", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/IProperty;)Ljava/lang/Comparable;"))
    public Comparable<Integer> arclight$blockFade(BlockState state, IProperty<Integer> property, BlockState blockState, ServerWorld worldIn, BlockPos pos, Random random) {
        Integer integer = state.get(property);
        if (integer == 7) {
            if (CraftEventFactory.callBlockFadeEvent(worldIn, pos, Blocks.AIR.getDefaultState()).isCancelled()) {
                return 6;
            } else {
                return integer;
            }
        } else {
            return integer;
        }
    }
}
