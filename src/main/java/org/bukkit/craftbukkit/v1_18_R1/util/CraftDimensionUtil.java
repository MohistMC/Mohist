package org.bukkit.craftbukkit.v1_18_R1.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class CraftDimensionUtil {

    private CraftDimensionUtil() {
    }

    public static ResourceKey<Level> getMainDimensionKey(Level world) {
        ResourceKey<Level> typeKey = world.dimension();
        if (typeKey == Level.OVERWORLD) {
            return Level.OVERWORLD;
        } else if (typeKey == Level.NETHER) {
            return Level.NETHER;
        } else if (typeKey == Level.END) {
            return Level.END;
        }

        return world.dimension();
    }
}
