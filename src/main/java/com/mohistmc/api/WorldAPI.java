package com.mohistmc.api;

import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.bukkit.World;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:49:40
 */
public class WorldAPI {

    public static Map<BlockPos, Entity> destroyBlockProgress = new HashMap<>();

    public static String getWorldName(World world) {
        if (ConfigByWorlds.config.get("worlds." + world.getName() + ".name") == null) {
            return world.getName();
        }
        return ConfigByWorlds.config.getString("worlds." + world.getName() + ".name");
    }
}
