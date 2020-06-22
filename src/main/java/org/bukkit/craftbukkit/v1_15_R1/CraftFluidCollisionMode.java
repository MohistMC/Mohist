package org.bukkit.craftbukkit.v1_15_R1;

import net.minecraft.util.math.RayTraceContext.FluidMode;
import org.bukkit.FluidCollisionMode;

public class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static FluidMode toNMS(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) return null;

        switch (fluidCollisionMode) {
            case ALWAYS:
                return FluidMode.ANY;
            case SOURCE_ONLY:
                return FluidMode.SOURCE_ONLY;
            case NEVER:
                return FluidMode.NONE;
            default:
                return null;
        }
    }
}
