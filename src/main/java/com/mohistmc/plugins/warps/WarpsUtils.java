package com.mohistmc.plugins.warps;

import com.mohistmc.util.YamlUtils;
import java.io.File;
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

    public static boolean has(String name) {
        return config.get(name) != null;
    }
}
