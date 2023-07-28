package com.mohistmc.plugins.world.listener;

import com.mohistmc.plugins.MessageI18N;
import com.mohistmc.plugins.world.commands.WorldsCommands;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Random;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:39:37
 */
public class InventoryClickListener {

    public static void createWorld(InventoryClickEvent event, Player p) {
        p.closeInventory();
        p.sendMessage(ChatColor.GREEN + "Start to create the world, there will be a little lag");
        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
        World.Environment environment = World.Environment.valueOf(itemName);
        String worldName = WorldsCommands.type;
        WorldCreator wc = new WorldCreator(worldName);
        wc.seed((new Random()).nextLong());
        wc.environment(environment);

        wc.createWorld();

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            String msg = String.format("Unable to create world %s. There may be more information in the console", worldName);
            p.sendMessage(ChatColor.RED + msg);
            return;
        }

        Location spawnLocation = world.getSpawnLocation();
        while (!spawnLocation.getBlock().getType().isAir() || !spawnLocation.getBlock().getRelative(BlockFace.UP).getType().isAir()) {
            spawnLocation.add(0, 1, 0);
        }

        world.setSpawnLocation(spawnLocation);
        p.teleport(spawnLocation);
        p.sendMessage(ChatColor.GREEN + "The world was created successfully");
        try {
            ConfigByWorlds.addWorld(world.getName());
            ConfigByWorlds.addSpawn(spawnLocation);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void init(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player p) {
            if (event.getView().getTitle().startsWith(MessageI18N.WORLDMANAGE_GUI_TITLE_0.getKey())) {
                try {
                    event.setCancelled(true);
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.MAP) {
                        createWorld(event, p);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else if (event.getView().getTitle().equals(MessageI18N.WORLDMANAGE_GUI_TITLE_1.getKey())) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null) {
                    return;
                }
                if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7>>")) {
                    String toSplit = event.getCurrentItem().getItemMeta().getDisplayName();
                    String[] splitted = toSplit.split("6");
                    if (Bukkit.getWorld(splitted[1]) != null) {
                        ConfigByWorlds.getSpawn(splitted[1], p);
                    } else {
                        WorldsCommands.worldNotExists(p, splitted[1]);
                    }
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageI18N.WORLDMANAGE_GUI_CLOSE.getKey())) {
                    p.closeInventory();
                }
            }
        }
    }
}
