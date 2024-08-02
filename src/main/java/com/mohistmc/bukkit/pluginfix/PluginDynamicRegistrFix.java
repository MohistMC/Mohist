package com.mohistmc.bukkit.pluginfix;

import java.util.List;
import org.bukkit.plugin.Plugin;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/12 2:52:18
 */
public class PluginDynamicRegistrFix {

    public static boolean canLock = true;
    private static final List<String> plugins = List.of("UltraCosmetics", "RealisticVillagers");

    public static void unlockRegistries(Plugin plugin) {
        if (plugins.contains(plugin.getName())) {
            canLock = false;
        }
    }

    public static void lockRegistries(Plugin plugin) {
        if (plugins.contains(plugin.getName())) {
            canLock = true;
        }
    }
}
