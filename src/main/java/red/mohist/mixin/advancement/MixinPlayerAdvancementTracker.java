package red.mohist.mixin.advancement;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import red.mohist.extra.advancement.ExtraAdvancement;

@Mixin(PlayerAdvancementTracker.class)
public class MixinPlayerAdvancementTracker {

    @Shadow
    private ServerPlayerEntity owner;

    @Inject(method = "grantCriterion", at = @At(
            value = "RETURN",
            target = "Lnet/minecraft/advancement/PlayerAdvancementTracker,grantCriterion(A a, S c)",
            remap = true
    ))
    public void mixinGrantCriterion(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir){
        this.owner.world.getServer().getPluginManager().callEvent(new org.bukkit.event.player.PlayerAdvancementDoneEvent(this.owner.getBukkitEntity(), ((ExtraAdvancement) (Object) this).getBukkit())); // CraftBukkit
    }
}
