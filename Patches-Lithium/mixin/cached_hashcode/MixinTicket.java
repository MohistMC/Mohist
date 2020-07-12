package me.jellysquid.mods.lithium.mixin.cached_hashcode;

import net.minecraft.world.server.Ticket;
import net.minecraft.world.server.TicketType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ticket.class)
public class MixinTicket<T> {
    @Shadow
    @Final
    private TicketType<T> type;

    @Shadow
    @Final
    private int level;

    @Shadow
    @Final
    private T value;

    private int hashCode;

    /**
     * @reason Initialize the object's hashcode and cache it
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(TicketType<T> type, int level, T argument, CallbackInfo ci) {
        int hash = 1;
        hash = 31 * hash + this.type.hashCode();
        hash = 31 * hash + this.level;
        hash = 31 * hash + this.value.hashCode();

        this.hashCode = hash;
    }

    /**
     * @reason Uses the cached hashcode
     * @author JellySquid
     */
    @Overwrite(remap = false)
    public int hashCode() {
        return this.hashCode;
    }
}
