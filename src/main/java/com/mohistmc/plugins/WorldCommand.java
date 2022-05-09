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

package com.mohistmc.plugins;

import com.mohistmc.api.ItemAPI;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class WorldCommand extends Command {

    public WorldCommand(String name) {
        super(name);
        this.usageMessage = "/world [list]";
        this.setPermission("mohist.command.world");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1 && args[0].equalsIgnoreCase("list") && sender.isOp()) {
                openWorldGui(p, "§8》 §6Worlds");
            }
        }
        return false;
    }

    public static void openWorldGui(Player p, String name) {
        int groesse = 54;
        int pos = 0;
        while (Bukkit.getWorlds().size() > groesse) {
            groesse += 54;
        }
        Inventory inv = Bukkit.createInventory(null, groesse, name);
        for (World w : Bukkit.getWorlds()) {
            ArrayList<String> infoLore = new ArrayList<String>();

            infoLore.add("§bWorldBorder §8》 §7" + w.getWorldBorder().getSize());
            infoLore.add("§bWorldType §8》 §7" + w.getEnvironment().name()); // TODO
            infoLore.add("§bDifficulty §8》 §7" + w.getDifficulty());
            infoLore.add("§bPlayers §8》 §7" + w.getPlayers().size());

            inv.setItem(pos, ItemAPI.doItem(Material.MAP, 1, 0, "§7》 §6" + w.getName().toString(), infoLore));
            ++pos;
            infoLore.clear();
        }
        inv.setItem(53, ItemAPI.doItem(Material.REDSTONE_BLOCK, 1, 0, "§4§lClose", null));
        p.openInventory(inv);
    }

    public static void onWorldManager(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player)event.getWhoClicked();
            if (event.getView().getTitle().equals("§8》 §6Worlds")) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
                    return;
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7》")) {
                    String toSplit = event.getCurrentItem().getItemMeta().getDisplayName();
                    String[] splitted = toSplit.split("6");
                    if (Bukkit.getWorld(splitted[1]) != null) {
                        p.teleport(Bukkit.getWorld(splitted[1]).getSpawnLocation());
                    }
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§4§lClose")) {
                    p.closeInventory();
                }
            }
        }
    }
}
