package io.izzel.arclight.common.mixin.core.entity.monster;

import io.izzel.arclight.common.bridge.entity.LivingEntityBridge;
import net.minecraft.entity.monster.IllusionerEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.entity.monster.IllusionerEntity.MirrorSpellGoal")
public class IllusionerEntity_MirrorSpellGoalMixin {

    @Shadow(aliases = {"this$0", "field_210767_a"}, remap = false) private IllusionerEntity outerThis;

    @Inject(method = "castSpell", at = @At("HEAD"))
    private void arclight$reason(CallbackInfo ci) {
        ((LivingEntityBridge) outerThis).bridge$pushEffectCause(EntityPotionEffectEvent.Cause.ILLUSION);
    }
}
