package com.mohistmc.bukkit.pluginfix;

import org.bukkit.plugin.Plugin;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/12 2:52:18
 */
public class UltraCosmetics {

    public static boolean canLock = true;

    public static void unlockRegistries(Plugin plugin) {
        if (plugin.getName().equals("UltraCosmetics")) {
            canLock = false;
        }
    }

    public static void lockRegistries(Plugin plugin) {
        if (plugin.getName().equals("UltraCosmetics")) {
            canLock = true;
        }
    }
}
