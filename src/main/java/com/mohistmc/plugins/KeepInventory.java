package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/24 2:54:00
 */
public class KeepInventory {

    public static boolean inventory(Level serverLevel) {
        String world = serverLevel.getWorld().getName();
        if (MohistConfig.keepinventory_global) {
            if (MohistConfig.keepinventory_inventory) {
                return true;
            }
        } else {
            if (MohistConfig.yml.getBoolean("keepinventory." + world + ".inventory")) {
                return true;
            }
        }
        return false;
    }

    public static int exp(Level serverLevel, int def) {
        String world = serverLevel.getWorld().getName();
        if (MohistConfig.keepinventory_global) {
            if (MohistConfig.keepinventory_exp) {
                return 0;
            }
        } else {
            if (MohistConfig.yml.getBoolean("keepinventory." + world + ".exp")) {
                return 0;
            }
        }
        return def;
    }
}
