package org.bukkit.craftbukkit.v1_20_R2.util;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/**
 * @deprecated legacy use only
 */
@Deprecated
public final class CraftLegacy {

    private static Material[] CACHE;
    private static int offset;

    private CraftLegacy() {
        //
    }

    public static Material fromLegacy(Material material) {
        if (material == null || !material.isLegacy()) {
            return material;
        }

        return org.bukkit.craftbukkit.v1_20_R2.legacy.CraftLegacy.fromLegacy(material);
    }

    public static Material fromLegacy(MaterialData materialData) {
        return org.bukkit.craftbukkit.v1_20_R2.legacy.CraftLegacy.fromLegacy(materialData);
    }

    public static Material[] modern_values() {
        if (CACHE == null) {
            int origin = Material.values().length;
            CACHE = Arrays.stream(Material.values()).filter(it -> !it.isLegacy()).toArray(Material[]::new);
            offset = origin - CACHE.length;
        }
        return Arrays.copyOf(CACHE, CACHE.length);
    }

    public static int modern_ordinal(Material material) {
        if (CACHE == null) {
            modern_values();
        }
        if (material.isLegacy()) {
            throw new NoSuchFieldError("Legacy field ordinal: " + material);
        } else {
            int ordinal = material.ordinal();
            return ordinal < Material.LEGACY_AIR.ordinal() ? ordinal : ordinal - offset;
        }
    }
}
