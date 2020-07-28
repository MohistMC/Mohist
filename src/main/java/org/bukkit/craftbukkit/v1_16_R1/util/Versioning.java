package org.bukkit.craftbukkit.v1_16_R1.util;

import org.bukkit.Bukkit;

public final class Versioning {

    public static String getBukkitVersion() {
        return Bukkit.getVersion(); // Returns: git-BukkitFabric-GITHASH
    }

}