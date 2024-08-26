package com.mohistmc.api;

import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import org.bukkit.World;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:49:40
 */
public class WorldAPI {

    public static String getWorldName(World world) {
        if (ConfigByWorlds.config.get("worlds." + world.getName() + ".name") == null) {
            return world.getName();
        }
        return ConfigByWorlds.config.getString("worlds." + world.getName() + ".name");
    }
}
