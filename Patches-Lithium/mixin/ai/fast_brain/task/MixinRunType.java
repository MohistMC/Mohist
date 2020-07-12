package me.jellysquid.mods.lithium.mixin.ai.fast_brain.task;

import me.jellysquid.mods.lithium.common.util.IIterableWeightedList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.WeightedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "net/minecraft/entity/ai/brain/task/MultiTask$RunType")
public class MixinRunType {
    @Mixin(targets = "net/minecraft/entity/ai/brain/task/MultiTask$RunType$1")
    public static class MixinRunOne {
        /**
         * @reason Replace stream code with traditional iteration
         * @author JellySquid
         */
        @Overwrite
        public <E extends LivingEntity> void func_220630_a(WeightedList<Task<? super E>> tasks, ServerWorld world, E entity, long time) {
            for (Task<? super E> task : IIterableWeightedList.cast(tasks)) {
                if (task.getStatus() == Task.Status.STOPPED) {
                    if (task.start(world, entity, time)) {
                        break;
                    }
                }
            }
        }
    }

    @Mixin(targets = "net/minecraft/entity/ai/brain/task/MultiTask$RunType$2")
    public static class MixinTryAll {
        /**
         * @reason Replace stream code with traditional iteration
         * @author JellySquid
         */
        @Overwrite
        public <E extends LivingEntity> void func_220630_a(WeightedList<Task<? super E>> tasks, ServerWorld world, E entity, long time) {
            for (Task<? super E> task : IIterableWeightedList.cast(tasks)) {
                if (task.getStatus() == Task.Status.STOPPED) {
                    task.start(world, entity, time);
                }
            }
        }
    }
}
