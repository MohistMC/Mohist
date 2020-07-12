package me.jellysquid.mods.lithium.mixin.avoid_allocations;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("InvalidMemberReference")
@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    private static final EquipmentSlotType[] SLOTS = EquipmentSlotType.values();

    /**
     * @reason Avoid cloning enum values
     */
    @Redirect(method = {"tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/EquipmentSlotType;values()[Lnet/minecraft/inventory/EquipmentSlotType;"))
    private EquipmentSlotType[] redirectEquipmentSlotsClone() {
        return SLOTS;
    }
}
