package me.jellysquid.mods.lithium.mixin.math.fast_util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AxisAlignedBB.class)
public class MixinAxisAlignedBB {
    @Shadow
    @Final
    public double minX;

    @Shadow
    @Final
    public double minY;

    @Shadow
    @Final
    public double maxY;

    @Shadow
    @Final
    public double minZ;

    @Shadow
    @Final
    public double maxX;

    @Shadow
    @Final
    public double maxZ;

    /**
     * @reason Simplify the code to better help the JVM optimize it
     * @author JellySquid
     */
    @Overwrite
    public double getMin(Direction.Axis axis) {
        switch (axis) {
            case X:
                return this.minX;
            case Y:
                return this.minY;
            case Z:
                return this.minZ;
        }

        throw new IllegalArgumentException();
    }

    /**
     * @reason Simplify the code to better help the JVM optimize it
     * @author JellySquid
     */
    @Overwrite
    public double getMax(Direction.Axis axis) {
        switch (axis) {
            case X:
                return this.maxX;
            case Y:
                return this.maxY;
            case Z:
                return this.maxZ;
        }

        throw new IllegalArgumentException();

    }
}
