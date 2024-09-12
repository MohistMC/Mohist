package com.mohistmc.plugins.world.listener;

import com.mohistmc.api.WorldAPI;
import com.mohistmc.plugins.world.commands.WorldsCommands;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import com.mohistmc.plugins.world.utils.WorldInventory;
import com.mohistmc.plugins.world.utils.WorldInventoryType;
import com.mohistmc.util.I18n;
import java.util.Random;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:39:37
 */
public class InventoryClickListener {

    public static WorldInventory worldInventory;

    public static void createWorld(InventoryClickEvent event, Player p) {
        p.closeInventory();
        String worldName = WorldsCommands.type;
        p.sendMessage(ChatColor.GREEN + I18n.as("worldlistener.ICL.worldCreateStart" , worldName));
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null && itemMeta.hasDisplayName()) {
            String itemName = itemMeta.getDisplayName();
            boolean isVoid = itemName.equals("void");
            WorldCreator wc = new WorldCreator(worldName);
            if (isVoid) wc.generator(new WorldAPI.VoidGenerator());
            wc.seed((new Random()).nextLong());
            wc.environment(isVoid ? Environment.NORMAL : World.Environment.valueOf(itemName));

            wc.createWorld();

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                String msg = String.format(I18n.as("worldlistener.ICL.worldCreateFailurePart1") + worldName) + I18n.as("worldlistener.ICL.worldCreateFailurePart2");
                p.sendMessage(ChatColor.RED + msg);
                return;
            }

            Location spawnLocation = world.getSpawnLocation();
            while (!spawnLocation.getBlock().getType().isAir() || !spawnLocation.getBlock().getRelative(BlockFace.UP).getType().isAir()) {
                spawnLocation.add(0, 1, 0);
            }

            world.setSpawnLocation(spawnLocation);
            p.sendMessage(ChatColor.GREEN + I18n.as("worldlistener.ICL.worldCreateSuccess" , worldName));
            try {
                ConfigByWorlds.addWorld(world.getName(), true);
                ConfigByWorlds.addSpawn(spawnLocation);
                if (isVoid) ConfigByWorlds.aVoid(world.getName(), isVoid);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }

    }

    public static void init(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (event.getWhoClicked() instanceof Player p) {
            if (worldInventory != null && worldInventory.getInventory() == inventory) {
                if (worldInventory.worldInventoryType() == WorldInventoryType.CREATE) {
                    event.setCancelled(true);

                    if (itemStack.getType() == Material.MAP) {
                        createWorld(event, p);
                    }
                } else if (worldInventory.worldInventoryType() == WorldInventoryType.LIST) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    if (itemMeta != null && itemMeta.hasDisplayName() && itemMeta.getDisplayName().startsWith("§7>>")) {
                        String toSplit = itemMeta.getDisplayName();
                        String[] splitted = toSplit.split("6");
                        if (Bukkit.getWorld(splitted[1]) != null) {
                            ConfigByWorlds.getSpawn(splitted[1], p);
                        } else {
                            WorldsCommands.worldNotExists(p, splitted[1]);
                        }
                    } else if (itemMeta != null && itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals(I18n.as("worldmanage.gui.close"))) {
                        p.closeInventory();
                    }
                }
            }
        }
    }
}
