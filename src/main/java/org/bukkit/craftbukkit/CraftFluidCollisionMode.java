package org.bukkit.craftbukkit;

import net.minecraft.util.math.RayTraceContext.FluidCollisionOption;
import org.bukkit.FluidCollisionMode;

public class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static FluidCollisionOption toNMS(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) return null;

        switch (fluidCollisionMode) {
            case ALWAYS:
                return FluidCollisionOption.ANY;
            case SOURCE_ONLY:
                return FluidCollisionOption.SOURCE_ONLY;
            case NEVER:
                return FluidCollisionOption.NONE;
            default:
                return null;
        }
    }
}
