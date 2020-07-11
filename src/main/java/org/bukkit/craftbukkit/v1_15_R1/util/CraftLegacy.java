package org.bukkit.craftbukkit.v1_15_R1.util;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/**
 * @deprecated legacy use only
 */
@Deprecated
public final class CraftLegacy {

    private static Material[] moddedMaterials;

    private CraftLegacy() {
        //
    }

    public static Material fromLegacy(Material material) {
        if (material == null || !material.isLegacy()) {
            return material;
        }

        return org.bukkit.craftbukkit.v1_15_R1.legacy.CraftLegacy.fromLegacy(material);
    }

    public static Material fromLegacy(MaterialData materialData) {
        return org.bukkit.craftbukkit.v1_15_R1.legacy.CraftLegacy.fromLegacy(materialData);
    }

    public static Material[] modern_values() {
        if (moddedMaterials == null) {
            moddedMaterials = Arrays.stream(Material.values()).filter(it -> !it.isLegacy()).toArray(Material[]::new);
        }
        return moddedMaterials;
    }


    public static int modern_ordinal(Material material) {
            if (material.isLegacy()) {
            // SPIGOT-4002: Fix for eclipse compiler manually compiling in default statements to lookupswitch
            throw new NoSuchFieldError("Legacy field ordinal: " + material);
        }

        return material.ordinal();
    }
}
