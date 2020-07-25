package io.izzel.arclight.common.mixin.core.inventory.container;

import io.izzel.arclight.common.bridge.entity.player.PlayerEntityBridge;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.util.IWorldPosCallable;
import org.bukkit.craftbukkit.v.inventory.CraftInventoryGrindstone;
import org.bukkit.craftbukkit.v.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.izzel.arclight.common.bridge.inventory.container.GrindstoneContainerBridge;

@Mixin(GrindstoneContainer.class)
public abstract class GrindstoneContainerMixin extends ContainerMixin implements GrindstoneContainerBridge {

    @Shadow @Final private IInventory inputInventory;
    @Shadow @Final private IInventory outputInventory;
    @Shadow @Final private IWorldPosCallable worldPosCallable;
    private CraftInventoryView bukkitEntity = null;
    private Player player;

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/util/IWorldPosCallable;)V", at = @At("RETURN"))
    public void arclight$init(int windowIdIn, PlayerInventory playerInventory, IWorldPosCallable worldPosCallableIn, CallbackInfo ci) {
        this.player = (Player) ((PlayerEntityBridge) playerInventory.player).bridge$getBukkitEntity();
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryGrindstone inventory = new CraftInventoryGrindstone(this.inputInventory, this.outputInventory);
        bukkitEntity = new CraftInventoryView(this.player, inventory, (Container) (Object) this);
        return bukkitEntity;
    }

    @Override
    public IWorldPosCallable bridge$getContainerAccess() {
        return this.worldPosCallable;
    }
}
