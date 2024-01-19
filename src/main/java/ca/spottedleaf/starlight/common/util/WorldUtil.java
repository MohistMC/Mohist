package ca.spottedleaf.starlight.common.util;

import net.minecraft.world.level.LevelHeightAccessor;

public final class WorldUtil {

    // min, max are inclusive

    public static int getMaxSection(final LevelHeightAccessor world) {
        return world.getMaxSection() - 1; // getMaxSection() is exclusive
    }

    public static int getMinSection(final LevelHeightAccessor world) {
        return world.getMinSection();
    }

    public static int getMaxLightSection(final LevelHeightAccessor world) {
        return getMaxSection(world) + 1;
    }

    public static int getMinLightSection(final LevelHeightAccessor world) {
        return getMinSection(world) - 1;
    }



    public static int getTotalSections(final LevelHeightAccessor world) {
        return getMaxSection(world) - getMinSection(world) + 1;
    }

    public static int getTotalLightSections(final LevelHeightAccessor world) {
        return getMaxLightSection(world) - getMinLightSection(world) + 1;
    }

    public static int getMinBlockY(final LevelHeightAccessor world) {
        return getMinSection(world) << 4;
    }

    public static int getMaxBlockY(final LevelHeightAccessor world) {
        return (getMaxSection(world) << 4) | 15;
    }

    private WorldUtil() {
        throw new RuntimeException();
    }

}
