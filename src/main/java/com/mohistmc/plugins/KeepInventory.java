package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import net.minecraft.world.level.Level;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/24 2:54:00
 */
public class KeepInventory {

    public static boolean inventory(Level serverLevel) {
        String world = serverLevel.getWorld().getName();
        if (MohistConfig.keepinventory_global) {
            return MohistConfig.keepinventory_inventory;
        } else {
            return MohistConfig.yml.getBoolean("keepinventory." + world + ".inventory");
        }
    }

    public static boolean exp(Level serverLevel) {
        String world = serverLevel.getWorld().getName();
        if (MohistConfig.keepinventory_global) {
            return MohistConfig.keepinventory_exp;
        } else {
            return MohistConfig.yml.getBoolean("keepinventory." + world + ".exp");
        }
    }
}
