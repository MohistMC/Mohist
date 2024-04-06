package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/24 2:54:00
 */
public class KeepInventory {

    public static boolean inventory(ServerPlayer player) {
        if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return true;
        }
        Player bukkit_player = player.getBukkitEntity();
        if (bukkit_player.hasPermission(MohistConfig.keepinventory_inventory_permission)) {
            return true;
        }
        String world = bukkit_player.getWorld().getName();
        boolean i = MohistConfig.keepinventory_global ? MohistConfig.keepinventory_inventory : MohistConfig.yml.getBoolean("keepinventory." + world + ".inventory");
        player.getBukkitEntity().getWorld().setGameRule(GameRule.KEEP_INVENTORY, i);
        return i;
    }

    public static boolean exp(ServerPlayer player) {
        if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return true;
        }
        Player bukkit_player = player.getBukkitEntity();
        if (bukkit_player.hasPermission(MohistConfig.keepinventory_exp_permission)) {
            return true;
        }
        String world = bukkit_player.getWorld().getName();
        boolean i = MohistConfig.keepinventory_global ? MohistConfig.keepinventory_exp : MohistConfig.yml.getBoolean("keepinventory." + world + ".exp");
        player.keepLevel = i;
        return i;
    }
}
