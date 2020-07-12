package me.jellysquid.mods.lithium.mixin.fast_tick_scheduler;

import me.jellysquid.mods.lithium.common.world.scheduler.LithiumServerTickScheduler;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.server.ServerTickList;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {
    /**
     * Redirects the creation of the vanilla server tick scheduler with our own.
     */
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/world/server/ServerTickList"))
    private <T> ServerTickList<T> redirectServerTickSchedulerCtor(ServerWorld world, Predicate<T> invalidPredicate, Function<T, ResourceLocation> idToName, Function<ResourceLocation, T> nameToId, Consumer<NextTickListEntry<T>> consumer) {
        return new LithiumServerTickScheduler<>(world, invalidPredicate, idToName, nameToId, consumer);
    }
}

