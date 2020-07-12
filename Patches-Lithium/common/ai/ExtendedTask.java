package me.jellysquid.mods.lithium.common.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.server.ServerWorld;

public interface ExtendedTask<E extends LivingEntity> {
    boolean bridge$shouldContinueExecuting(ServerWorld world, E entity, long time);
}
