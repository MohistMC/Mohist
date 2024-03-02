package com.mohistmc.plugins.warps;

import com.mohistmc.util.I18n;
import com.mohistmc.util.YamlUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/12 16:39:15
 */
public class WarpsUtils {

    public static File f = new File("mohist-config", "warps.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(f);

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
        for (String w : WarpsUtils.config.getKeys(false)) {
            Location warpLoc = WarpsUtils.get(w);
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
        return WarpsUtils.config.getKeys(false).stream().map(WarpsUtils::get).collect(Collectors.toList());
    }


    public static boolean has(String name) {
        return config.get(name) != null;
    }
}
