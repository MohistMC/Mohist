/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
