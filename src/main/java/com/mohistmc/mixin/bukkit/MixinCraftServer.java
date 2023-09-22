package com.mohistmc.mixin.bukkit;

import com.mohistmc.MohistMC;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/17 7:41:02
 */
@Mixin(value = CraftServer.class, remap = false)
public class MixinCraftServer {

    @Inject(method = "loadPlugins", remap = false, at = @At("HEAD"))
    private void helloWorld(CallbackInfo ci) {
       MohistMC.LOGGER.info("Hello Plugins!");
    }
}
