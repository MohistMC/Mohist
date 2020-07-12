package me.jellysquid.mods.lithium.mixin.ai.nearby_entity_tracking;

import me.jellysquid.mods.lithium.common.entity.tracker.EntityTrackerEngine;
import me.jellysquid.mods.lithium.common.entity.tracker.WorldWithEntityTrackerEngine;
import net.minecraft.profiler.IProfiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

/**
 * Extends the base world class to provide a {@link EntityTrackerEngine}.
 */
@Mixin(World.class)
public class MixinWorld implements WorldWithEntityTrackerEngine {
    private EntityTrackerEngine tracker;

    /**
     * Initialize the {@link EntityTrackerEngine} which all entities of the world will interact with.
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(WorldInfo levelProperties, DimensionType dimensionType, BiFunction<World, Dimension, AbstractChunkProvider> chunkManagerProvider, IProfiler profiler, boolean isClient, CallbackInfo ci) {
        this.tracker = new EntityTrackerEngine();
    }

    @Override
    public EntityTrackerEngine getEntityTracker() {
        return this.tracker;
    }
}
