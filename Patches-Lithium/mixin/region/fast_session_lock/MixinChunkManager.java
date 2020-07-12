package me.jellysquid.mods.lithium.mixin.region.fast_session_lock;

import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SessionLockException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Minecraft checks the session lock every time a chunk is saved while flushing chunks to disk. By changing the logic
 * to only check once at the beginning of the flush phase, a number of I/O operations can be avoided.
 */
@Mixin(ChunkManager.class)
public abstract class MixinChunkManager {
    @Shadow
    @Final
    private ServerWorld world;

    private boolean hasCheckedLock;

    /**
     * Reset the flag which indicates whether or not the check has been performed.
     */
    @Inject(method = "save(Z)V", at = @At("HEAD"))
    private void beforeSave(boolean flush, CallbackInfo ci) {
        this.hasCheckedLock = false;
    }

    /**
     * Cancel checking the session lock if we have already done so for this save phase.
     */
    @Redirect(method = "func_219229_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/server/ServerWorld;checkSessionLock()V"))
    private void nullifySessionLock(ServerWorld serverWorld) throws SessionLockException {
        if (!this.hasCheckedLock) {
            this.world.checkSessionLock();

            this.hasCheckedLock = true;
        }
    }
}
