package com.mohistmc.command;

import com.mohistmc.util.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.minecraft.command.CommandBase.parseInt;

public class GiveCommand {

    public static boolean info(CommandSender sender, String[] args) {
        if (args.length >= 3) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) return false;
            String args2 = args[2];
            String[] s = args2.split(":");
            String j = args2.contains(":") ? s[1] : String.valueOf(0);
            // mohist give Mgazul 4096 64
            if (NumberUtils.isInteger(args2)) {
                int id = Integer.valueOf(args2).intValue();
                if (Material.byId[id] != null) {
                    int i = args.length == 4 ? Integer.parseInt(args[3]) : 1;
                    ItemStack itemStack = new ItemStack(Material.getMaterial(id), i, Short.parseShort(j));
                    if (itemStack != null) {
                        player.getInventory().addItem(itemStack);
                    }
                }
            }
            // mohist give Mgazul 4096:1 64
            else if (args2.contains(":") && NumberUtils.isInteger(s[0]) && NumberUtils.isInteger(s[1])) {
                int id = Integer.valueOf(s[0]).intValue();
                int i = args.length == 4 ? Integer.parseInt(args[3]) : 1;
                ItemStack itemStack = new ItemStack(Material.getMaterial(id), i, Short.parseShort(s[1]));
                if (itemStack != null) {
                    player.getInventory().addItem(itemStack);
                }
            }
            // mohist give Mgazul ASDASD 64
            else if (Material.getMaterial(args2) != null) {
                int i = args.length == 4 ? Integer.parseInt(args[3]) : 1;
                ItemStack itemStack = new ItemStack(Material.getMaterial(args2), i, Short.parseShort(j));
                if (itemStack != null) {
                    player.getInventory().addItem(itemStack);
                }
            }
        }
        return false;
    }
}
