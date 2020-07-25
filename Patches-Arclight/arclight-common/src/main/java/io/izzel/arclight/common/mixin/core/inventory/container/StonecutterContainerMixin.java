package io.izzel.arclight.common.mixin.core.inventory.container;

import io.izzel.arclight.common.bridge.entity.player.ServerPlayerEntityBridge;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.StonecutterContainer;
import net.minecraft.util.IWorldPosCallable;
import org.bukkit.craftbukkit.v.inventory.CraftInventoryStonecutter;
import org.bukkit.craftbukkit.v.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StonecutterContainer.class)
public abstract class StonecutterContainerMixin extends ContainerMixin {

    // @formatter:off
    @Shadow @Final public IInventory inputInventory;
    @Shadow @Final private CraftResultInventory inventory;
    // @formatter:on

    private CraftInventoryView bukkitEntity = null;
    private Player player;

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/util/IWorldPosCallable;)V", at = @At("RETURN"))
    public void arclight$init(int windowIdIn, PlayerInventory playerInventoryIn, IWorldPosCallable worldPosCallableIn, CallbackInfo ci) {
        this.player = ((ServerPlayerEntityBridge) playerInventoryIn.player).bridge$getBukkitEntity();
    }

    @Override
    public InventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryStonecutter inventory = new CraftInventoryStonecutter(this.inputInventory, this.inventory);
        bukkitEntity = new CraftInventoryView(this.player, inventory, (Container) (Object) this);
        return bukkitEntity;
    }
}
