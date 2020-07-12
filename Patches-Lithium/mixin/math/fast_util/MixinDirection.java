package me.jellysquid.mods.lithium.mixin.math.fast_util;

import net.minecraft.util.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Direction.class)
public class MixinDirection {
    @Shadow
    @Final
    private static Direction[] VALUES;

    @Shadow
    @Final
    private int opposite;

    /**
     * @reason Avoid the modulo/abs operations
     * @author JellySquid
     */
    @Overwrite
    public Direction getOpposite() {
        return VALUES[this.opposite];
    }
}
