package com.mohistmc.mixin.minecraft;

import com.mohistmc.MohistMC;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public abstract class MixinDedicatedServer {

    @Inject(method = "initServer", at = @At("HEAD"))
    private void helloWorld(CallbackInfoReturnable<Boolean> cir) {
        MohistMC.LOGGER.info("Hello DedicatedServer!");
    }
}