package com.mohistmc.api;

import com.mohistmc.api.location.LocationAPI;
import com.mohistmc.util.I18n;
import com.mohistmc.util.YamlUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WarpAPI {

    public static File f = new File("mohist-config", "warps.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(f);

    /**
     * Teleports the given player to a specific location.
     *
     * @param player The Player object to be teleported.
     * @param warpsName The name of the warp point where the player should be teleported to.
     * This method invokes `WarpsUtils.get(warpsName)` to obtain the destination based on the provided warp point name,
     * and then teleports the player to that location.
     */
    public static void teleport(Player player, String warpsName) {
        player.teleport(get(warpsName));
    }

    public static void teleportSafe(Player player, String warpsName, int distance) {
        if (LocationAPI.distanceBetweenLocation(player.getLocation(), get(warpsName)) > distance) {
            player.teleport(get(warpsName));
        }
    }

    public static void init() {
        YamlUtils.save(f, config);
    }

    public static void add(Location location, String name) {
        config.set(name, location);
        init();
    }

    public static void del(String name) {
        config.set(name, null);
        init();
    }

    public static Location get(String name) {
        return config.getLocation(name);
    }

    public static String getName(Location location) {
        for (String w : config.getKeys(false)) {
            Location warpLoc = get(w);
            if (location.equals(warpLoc)) {
                return w;
            }
        }
        return null;
    }

    public static List<String> asStringList(String name) {
        String locationAsString = get(name).asString();
        List<String> linesList = new ArrayList<>();
        linesList.add(I18n.as("warpscommands.gui.click"));
        String[] parts = locationAsString.split(",");
        for (String part : parts) {
            linesList.add("§f" + part.trim());
        }
        return linesList;
    }

    public static List<Location> asList() {
        return config.getKeys(false).stream().map(WarpAPI::get).collect(Collectors.toList());
    }

    public static boolean has(String name) {
        return config.get(name) != null;
    }
}
