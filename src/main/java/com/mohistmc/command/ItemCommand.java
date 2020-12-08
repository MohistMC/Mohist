package com.mohistmc.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand{

    public static void info(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            // item name and i18n name
            player.sendMessage(ChatColor.GRAY + "Name - " + ChatColor.GREEN + itemStack.getType().toString());
            player.sendMessage(ChatColor.GRAY + "ForgeBlock - " + (itemStack.getType().isForgeBlock() ? Boolean.valueOf(true) : Boolean.valueOf(false)));
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to perform this command.");
        }
    }
}
