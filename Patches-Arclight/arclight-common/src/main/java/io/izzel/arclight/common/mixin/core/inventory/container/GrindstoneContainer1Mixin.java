package io.izzel.arclight.common.mixin.core.inventory.container;

import io.izzel.arclight.common.bridge.util.IWorldPosCallableBridge;
import io.izzel.arclight.common.mixin.core.inventory.InventoryMixin;
import net.minecraft.inventory.container.GrindstoneContainer;
import org.bukkit.Location;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import io.izzel.arclight.common.bridge.inventory.container.GrindstoneContainerBridge;

@Mixin(targets = "net/minecraft/inventory/container/GrindstoneContainer$1")
public abstract class GrindstoneContainer1Mixin extends InventoryMixin {

    @Shadow(aliases = {"this$0", "field_213912_a"}, remap = false) private GrindstoneContainer outerThis;

    @Override
    public Location getLocation() {
        return ((IWorldPosCallableBridge) ((GrindstoneContainerBridge) outerThis).bridge$getContainerAccess()).bridge$getLocation();
    }
}
