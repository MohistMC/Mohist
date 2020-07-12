package me.jellysquid.mods.lithium.mixin.avoid_allocations;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.server.ServerChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerChunkProvider.class)
public abstract class MixinServerChunkProvider {
    private static final EntityClassification[] ENTITY_CATEGORIES = EntityClassification.values();

    /**
     * @reason Avoid cloning enum values
     */
    @Redirect(method = "tickChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityClassification;values()[Lnet/minecraft/entity/EntityClassification;"))
    private EntityClassification[] redirectEntityCategoryValues() {
        return ENTITY_CATEGORIES;
    }
}
