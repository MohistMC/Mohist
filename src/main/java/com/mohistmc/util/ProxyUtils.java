package com.mohistmc.util;

import com.mohistmc.MohistConfig;
import org.spigotmc.SpigotConfig;

public class ProxyUtils {

    public static boolean is() {
        return MohistConfig.velocity_enabled || SpigotConfig.bungee;
    }

    public static boolean isProxyOnlineMode() {
        return org.bukkit.Bukkit.getOnlineMode() || (org.spigotmc.SpigotConfig.bungee) || (MohistConfig.velocity_enabled && MohistConfig.velocity_onlineMode);
    }
}
