package red.mohist.extra;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.server.ServerWorld;

public interface ExtraExtendTask<E extends LivingEntity> {
    boolean shouldContinueExecuting(ServerWorld world, E entity, long time);
}
