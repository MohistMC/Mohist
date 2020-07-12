package me.jellysquid.mods.lithium.mixin.ai.fast_goal_selection;

import me.jellysquid.mods.lithium.common.ai.ExtendedGoal;
import net.minecraft.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(Goal.class)
public class MixinGoal implements ExtendedGoal {
    private static final Goal.Flag[] NO_CONTROLS = new Goal.Flag[0];

    private Goal.Flag[] controlsArray = NO_CONTROLS;

    /**
     * Initialize our flat controls array to mirror the vanilla EnumSet.
     */
    @Inject(method = "setMutexFlags", at = @At("RETURN"))
    private void onFlagsUpdated(EnumSet<Goal.Flag> set, CallbackInfo ci) {
        this.controlsArray = set.toArray(NO_CONTROLS); // NO_CONTROLS is only used to get around type erasure
    }

    @Override
    public Goal.Flag[] getRequiredControls() {
        return controlsArray;
    }
}
