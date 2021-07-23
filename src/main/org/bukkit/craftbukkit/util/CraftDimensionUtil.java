package org.bukkit.craftbukkit.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class CraftDimensionUtil {

    private CraftDimensionUtil() {
    }

    public static ResourceKey<Level> getMainDimensionKey(Level world) {
        ResourceKey<DimensionType> typeKey = world.getTypeKey();
        if (typeKey == DimensionType.OVERWORLD_LOCATION) {
            return Level.OVERWORLD;
        } else if (typeKey == DimensionType.NETHER_LOCATION) {
            return Level.NETHER;
        } else if (typeKey == DimensionType.END_LOCATION) {
            return Level.END;
        }

        return world.dimension();
    }
}
