package org.bukkit.craftbukkit;

import net.minecraft.world.RayTraceContext.FluidHandling;
import org.bukkit.FluidCollisionMode;

public final class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static FluidHandling toNMS(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) {
            return null;
        }

        switch (fluidCollisionMode) {
            case ALWAYS:
                return FluidHandling.ANY;
            case SOURCE_ONLY:
                return FluidHandling.SOURCE_ONLY;
            case NEVER:
                return FluidHandling.NONE;
            default:
                return null;
        }
    }
}
