package me.jellysquid.mods.lithium.mixin.math.fast_util;

import net.minecraft.util.Direction;
import net.minecraft.util.AxisRotation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AxisRotation.class)
public class MixinAxisRotation {
    @Mixin(targets = "net/minecraft/util/AxisRotation$2")
    public static class MixinForward {
        /**
         * @reason Avoid expensive array/modulo operations
         * @author JellySquid
         */
        @Overwrite
        public Direction.Axis rotate(Direction.Axis axis) {
            switch (axis) {
                case X:
                    return Direction.Axis.Y;
                case Y:
                    return Direction.Axis.Z;
                case Z:
                    return Direction.Axis.X;
            }

            throw new IllegalArgumentException();
        }
    }

    @Mixin(targets = "net/minecraft/util/AxisRotation$3")
    public static class MixinBackward {
        /**
         * @reason Avoid expensive array/modulo operations
         * @author JellySquid
         */
        @Overwrite
        public Direction.Axis rotate(Direction.Axis axis) {
            switch (axis) {
                case X:
                    return Direction.Axis.Z;
                case Y:
                    return Direction.Axis.X;
                case Z:
                    return Direction.Axis.Y;
            }

            throw new IllegalArgumentException();
        }
    }
}
