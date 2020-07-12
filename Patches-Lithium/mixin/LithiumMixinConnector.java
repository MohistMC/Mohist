package me.jellysquid.mods.lithium.mixin;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class LithiumMixinConnector implements IMixinConnector {
    @Override
    public void connect() {
        Mixins.addConfiguration("lithium.mixins.json");
    }
}
