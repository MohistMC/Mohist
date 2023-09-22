package com.mohistmc.plugins;

import com.mohistmc.api.WorldAPI;
import com.mohistmc.plugins.world.WorldDate;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MohistPapiHook extends PlaceholderExpansion {

    private static String hook_name = "mohist";

    public static void init() {
        new MohistPapiHook().register();
    }

    public static String replace(OfflinePlayer player, String x) {
        return PlaceholderAPI.setPlaceholders(player, x.replace("&", "§"));
    }

    @Override
    public String onPlaceholderRequest(Player p, String i) {
        if (p == null) {
            return null;
        }
        if (i.equalsIgnoreCase("world_name")) {
            return WorldAPI.getDate(p.getWorld(), WorldDate.NAME).replace("&", "§");
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return hook_name;
    }

    @Override
    public String getAuthor() {
        return "MohistMC";
    }

    @Override
    public String getVersion() {
        return "1.20.2";
    }
}
