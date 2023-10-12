package com.mohistmc.api;

import com.mohistmc.plugins.world.WorldDate;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import java.util.Locale;
import org.bukkit.World;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:49:40
 */
public class WorldAPI {

    public static String getDate(World world, WorldDate worldDate) {
        if (ConfigByWorlds.config.getString("worlds." + world.getName() + "." + worldDate.name().toLowerCase(Locale.ENGLISH)) == null) {
            switch (worldDate) {
                case NAME -> {
                    return world.getName();
                }
                case INFO -> {
                    return "-/-";
                }
                default -> throw new IllegalStateException("Unexpected value: " + worldDate);
            }
        }
        return ConfigByWorlds.config.getString("worlds." + world.getName() + "." + worldDate.name().toLowerCase(Locale.ENGLISH));
    }
}
