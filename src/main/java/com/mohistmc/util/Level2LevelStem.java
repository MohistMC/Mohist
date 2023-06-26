package com.mohistmc.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import org.bukkit.World;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mgazul by MohistMC
 * @date 2023/5/25 21:28:44
 */
public class Level2LevelStem {

    public static AtomicBoolean initPluginWorld = new AtomicBoolean(false); // Mohist
    public static Path worldPath_cache; // Mohist
    public static final Map<String, World> plugin_worlds = new LinkedHashMap<>();

    public static void reloadAndInit(World world) {
        Level2LevelStem.plugin_worlds.put("name", world); // Add to cache
        Level2LevelStem.initPluginWorld.set(false); // check is plugin
        Level2LevelStem.worldPath_cache = null;
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

    public static ResourceKey<LevelStem> getTypeKey(Level p_53026_) {
        if (p_53026_.dimension() == Level.END) {
            return LevelStem.END;
        } else if (p_53026_.dimension() == Level.NETHER) {
            return LevelStem.NETHER;
        }else  {
            return LevelStem.OVERWORLD;
        }
    }
}
