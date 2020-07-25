package io.izzel.arclight.common.mixin.core.block;

import io.izzel.arclight.common.bridge.entity.EntityBridge;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v.block.CraftBlock;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(AbstractButtonBlock.class)
public class AbstractButtonBlockMixin {

    // @formatter:off
    @Shadow @Final public static BooleanProperty POWERED;
    // @formatter:on

    @Inject(method = "checkPressed", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/IProperty;)Ljava/lang/Comparable;"))
    public void arclight$entityInteract(BlockState state, World worldIn, BlockPos pos, CallbackInfo ci,
                                        List<? extends Entity> list, boolean flag) {
        boolean flag1 = state.get(AbstractButtonBlock.POWERED);
        if (flag1 != flag && flag) {
            Block block = CraftBlock.at(worldIn, pos);
            boolean allowed = false;

            for (Object object : list) {
                if (object != null) {
                    EntityInteractEvent event = new EntityInteractEvent(((EntityBridge) object).bridge$getBukkitEntity(), block);
                    Bukkit.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        allowed = true;
                        break;
                    }
                }
            }

            if (!allowed) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "checkPressed", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    public void arclight$blockRedstone3(BlockState state, World worldIn, BlockPos pos, CallbackInfo ci,
                                        List<? extends Entity> list, boolean flag, boolean flag1) {
        Block block = CraftBlock.at(worldIn, pos);
        int old = (flag1) ? 15 : 0;
        int current = (!flag1) ? 15 : 0;

        BlockRedstoneEvent event = new BlockRedstoneEvent(block, old, current);
        Bukkit.getPluginManager().callEvent(event);

        if ((flag && event.getNewCurrent() <= 0) || (!flag && event.getNewCurrent() > 0)) {
            ci.cancel();
        }
    }
}
