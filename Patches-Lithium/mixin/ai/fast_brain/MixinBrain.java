package me.jellysquid.mods.lithium.mixin.ai.fast_brain;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

/**
 * A significant amount of overhead in entity ticking comes from mob brains iterating over their list of tasks. This
 * patch replaces the stream-based code with more traditional iteration and then flattens out the nested iteration
 * into simple arrays that can be quickly scanned over, providing a massive speedup and reduction to memory
 * allocations.
 */
@Mixin(Brain.class)
public abstract class MixinBrain<E extends LivingEntity> {
    @Shadow
    @Final
    private Map<SensorType<? extends Sensor<? super E>>, Sensor<? super E>> sensors;

    @Shadow
    @Final
    private Map<Integer, Map<Activity, Set<Task<? super E>>>> field_218232_c;

    @Shadow
    @Final
    private Set<Activity> activities;

    private final List<Pair<Activity, List<Task<? super E>>>> allTasks = new ArrayList<>();

    @Inject(method = "registerActivity(Lnet/minecraft/entity/ai/brain/schedule/Activity;Lcom/google/common/collect/ImmutableList;Ljava/util/Set;)V", at = @At("RETURN"))
    private void onTaskListUpdated(Activity activity, ImmutableList<Pair<Integer, ? extends Task<? super E>>> immutableList_1, Set<Pair<MemoryModuleType<?>, MemoryModuleStatus>> set_1, CallbackInfo ci) {
        this.allTasks.clear();

        // Re-build the sorted list of tasks, flattening it into a simple array
        // This will only happen a few times during during entity initialization
        for (Map<Activity, Set<Task<? super E>>> map : this.field_218232_c.values()) {
            for (Map.Entry<Activity, Set<Task<? super E>>> entry : map.entrySet()) {
                this.allTasks.add(Pair.of(entry.getKey(), new ArrayList<>(entry.getValue())));
            }
        }
    }

    /**
     * @reason Replace stream-based code with traditional iteration
     * @author JellySquid
     */
    @Overwrite
    private void updateSensors(ServerWorld world, E entity) {
        for (Sensor<? super E> sensor : this.sensors.values()) {
            sensor.tick(world, entity);
        }
    }

    /**
     * @reason Replace stream-based code with traditional iteration, use flattened collection type
     * @author JellySquid
     */
    @Overwrite
    private void startTasks(ServerWorld world, E entity) {
        long time = world.getGameTime();

        for (Pair<Activity, List<Task<? super E>>> pair : this.allTasks) {
            if (!this.activities.contains(pair.getFirst())) {
                continue;
            }

            for (Task<? super E> task : pair.getSecond()) {
                if (task.getStatus() == Task.Status.STOPPED) {
                    task.start(world, entity, time);
                }
            }
        }
    }

    /**
     * @reason Replace stream-based code with traditional iteration, use flattened collection type
     * @author JellySquid
     */
    @Overwrite
    private void tickTasks(ServerWorld world, E entity) {
        long time = world.getGameTime();

        for (Pair<Activity, List<Task<? super E>>> pair : this.allTasks) {
            for (Task<? super E> task : pair.getSecond()) {
                if (task.getStatus() == Task.Status.RUNNING) {
                    task.tick(world, entity, time);
                }
            }
        }
    }
}
