package me.jellysquid.mods.lithium.mixin.ai.fast_brain.task;

import me.jellysquid.mods.lithium.common.ai.ExtendedTask;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.Tuple;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(Task.class)
public abstract class MixinTask<E extends LivingEntity> implements ExtendedTask<E> {
    @Shadow
    @Final
    private Map<MemoryModuleType<?>, MemoryModuleStatus> requiredMemoryState;

    @Shadow
    protected abstract boolean shouldContinueExecuting(ServerWorld worldIn, E entityIn, long gameTimeIn);

    private List<Tuple<MemoryModuleType<?>, MemoryModuleStatus>> requiredMemoryStatesFlattened;

    @Inject(method = "<init>(Ljava/util/Map;II)V", at = @At("RETURN"))
    private void init(Map<MemoryModuleType<?>, MemoryModuleStatus> map, int int_1, int int_2, CallbackInfo ci) {
        List<Tuple<MemoryModuleType<?>, MemoryModuleStatus>> flattened = new ArrayList<>(map.size());

        for (Map.Entry<MemoryModuleType<?>, MemoryModuleStatus> entry : this.requiredMemoryState.entrySet()) {
            flattened.add(new Tuple<>(entry.getKey(), entry.getValue()));
        }

        this.requiredMemoryStatesFlattened = flattened;
    }

    /**
     * @reason Replace stream-based code with traditional iteration, use a flattened array list to avoid pointer chasing
     * @author JellySquid
     */
    @Overwrite
    private boolean hasRequiredMemories(E entity) {
        for (Tuple<MemoryModuleType<?>, MemoryModuleStatus> entry : this.requiredMemoryStatesFlattened) {
            if (!entity.getBrain().hasMemory(entry.getA(), entry.getB())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean bridge$shouldContinueExecuting(ServerWorld world, E entity, long time) {
        return this.shouldContinueExecuting(world, entity, time);
    }
}
