package red.mohist.api.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Mgazul
 * @date 2019/12/21 9:13
 */
@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;setGuiEnabled()V"))
    private static void guiEnabled(DedicatedServer dedicatedServer) {
    }
}
