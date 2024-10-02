package com.mohistmc.util;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.World;

/**
 * @author Mgazul by MohistMC
 * @date 2023/5/25 21:28:44
 */
public class Level2LevelStem {

    public static AtomicBoolean initPluginWorld = new AtomicBoolean(false); // Mohist
    public static Path worldPath_cache; // Mohist
    public static final Map<String, World> plugin_worlds = new LinkedHashMap<>();
    public static File bukkit;
    public static String bukkit_name;

    public static void reloadAndInit(World world) {
        Level2LevelStem.plugin_worlds.put(world.getName(), world); // Add to cache
        Level2LevelStem.initPluginWorld.set(false); // check is plugin
        Level2LevelStem.worldPath_cache = null;
        Level2LevelStem.bukkit = null;
        Level2LevelStem.bukkit_name = null;
    }

    public static Path checkPath(Path path) {
        if (Level2LevelStem.initPluginWorld.get()) {
            if (Level2LevelStem.worldPath_cache == null) {
                Level2LevelStem.worldPath_cache = path;
            } else {
                return worldPath_cache;
            }
        }
        return path;
    }
}
