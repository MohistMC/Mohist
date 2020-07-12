package me.jellysquid.mods.lithium.mixin.ai.nearby_entity_tracking.goals;

import me.jellysquid.mods.lithium.common.entity.tracker.nearby.EntityWithNearbyListener;
import me.jellysquid.mods.lithium.common.entity.tracker.nearby.NearbyEntityTracker;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IEntityReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookAtGoal.class)
public class MixinLookAtGoal {
    private NearbyEntityTracker<? extends LivingEntity> tracker;

    @Inject(method = "<init>(Lnet/minecraft/entity/MobEntity;Ljava/lang/Class;FF)V", at = @At("RETURN"))
    private void init(MobEntity mob, Class<? extends LivingEntity> targetType, float range, float chance, CallbackInfo ci) {
        this.tracker = new NearbyEntityTracker<>(targetType, mob, range);

        ((EntityWithNearbyListener) mob).getListener().addListener(this.tracker);
    }

    @Redirect(method = "shouldExecute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IEntityReader;func_225318_b(Ljava/lang/Class;Lnet/minecraft/entity/EntityPredicate;Lnet/minecraft/entity/LivingEntity;DDDLnet/minecraft/util/math/AxisAlignedBB;)Lnet/minecraft/entity/LivingEntity;"))
    private <T extends LivingEntity> LivingEntity redirectGetClosestEntity(IEntityReader world, Class<? extends T> p_225318_1_, EntityPredicate p_225318_2_, LivingEntity p_225318_3_, double p_225318_4_, double p_225318_6_, double p_225318_8_, AxisAlignedBB p_225318_10_) {
        return this.tracker.getClosestEntity();
    }

    @Redirect(method = "shouldExecute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IEntityReader;getClosestPlayer(Lnet/minecraft/entity/EntityPredicate;Lnet/minecraft/entity/LivingEntity;DDD)Lnet/minecraft/entity/player/PlayerEntity;"))
    private PlayerEntity redirectGetClosestPlayer(IEntityReader world, EntityPredicate targetPredicate, LivingEntity entity, double x, double y, double z) {
        return (PlayerEntity) this.tracker.getClosestEntity();
    }
}
