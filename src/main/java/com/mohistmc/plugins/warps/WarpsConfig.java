package com.mohistmc.plugins.warps;

import com.mohistmc.plugins.config.MohistPluginConfig;
import java.io.File;
import org.bukkit.Location;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/12 16:39:15
 */
public class WarpsConfig extends MohistPluginConfig {

    public static WarpsConfig INSTANCE;

    public WarpsConfig(File file) {
        super(file);
    }

    public static void init() {
        INSTANCE = new WarpsConfig(new File("mohist-config", "warps.yml"));
    }

    public Location get(String name) {
        return yaml.getLocation(name);
    }
}
