package com.mohistmc.plugins.world.listener;

import com.mohistmc.plugins.MessageI18N;
import com.mohistmc.plugins.world.commands.WorldsCommands;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:39:37
 */
public class InventoryClickListener {

    public static void createWorld(InventoryClickEvent event, Player p) {
        p.closeInventory();
        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
        World.Environment environment = World.Environment.valueOf(itemName);
        World w = Bukkit.createWorld(new WorldCreator(WorldsCommands.type).environment(environment));
        Location location = Bukkit.getWorld(WorldsCommands.type).getSpawnLocation();
        p.teleport(location);
        try {
            ConfigByWorlds.addWorld(w.getName());
            ConfigByWorlds.addSpawn(location);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void init(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player p) {
            if (event.getView().getTitle().startsWith(MessageI18N.WORLDMANAGE_GUI_TITLE_0.getKey())) {
                try {
                    if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.MAP) {
                        createWorld(event, p);
                    }
                }
                catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
            else if (event.getView().getTitle().equals(MessageI18N.WORLDMANAGE_GUI_TITLE_1.getKey())) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null) {
                    return;
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7>>")) {
                    String toSplit = event.getCurrentItem().getItemMeta().getDisplayName();
                    String[] splitted = toSplit.split("6");
                    if (Bukkit.getWorld(splitted[1]) != null) {
                        ConfigByWorlds.getSpawn(splitted[1], p);
                    }
                    else {
                        WorldsCommands.worldNotExists(p, splitted[1]);
                    }
                }
                else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageI18N.WORLDMANAGE_GUI_CLOSE.getKey())) {
                    p.closeInventory();
                }
            }
        }
    }
}
