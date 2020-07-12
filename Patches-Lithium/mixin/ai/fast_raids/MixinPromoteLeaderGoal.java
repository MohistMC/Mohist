package me.jellysquid.mods.lithium.mixin.ai.fast_raids;

import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.world.raid.Raid;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractRaiderEntity.PromoteLeaderGoal.class)
public class MixinPromoteLeaderGoal {
    // The call to Raid#getOminousBanner() is very expensive, so cache it and re-use it during AI ticking
    private static final ItemStack CACHED_ILLAGER_BANNER = Raid.createIllagerBanner();

    @Redirect(method = "shouldExecute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/raid/Raid;createIllagerBanner()Lnet/minecraft/item/ItemStack;"))
    private ItemStack getOminousBanner() {
        return CACHED_ILLAGER_BANNER;
    }
}