package com.mohistmc.plugins.world.utils;

import com.mohistmc.api.ItemAPI;
import com.mohistmc.plugins.world.listener.InventoryClickListener;
import com.mohistmc.util.I18n;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:57:28
 */
public class WorldsGUI {

    public static void openWorldGui(Player p, String name) {
        int groesse = 54;
        int pos = 0;
        while (Bukkit.getWorlds().size() > groesse) {
            groesse += 54;
        }
        WorldInventory worldListInventory = new WorldInventory(WorldInventoryType.LIST, groesse, name);
        Inventory inv = worldListInventory.getInventory();
        for (World w : Bukkit.getWorlds()) {
            ArrayList<String> infoLore = new ArrayList<>();
            FileConfiguration config = ConfigByWorlds.config;
            if (ConfigByWorlds.f.exists() && config.getConfigurationSection("worlds.") != null) {
                String worldtype = w.getEnvironment() == null ? "null" : w.getEnvironment().name();
                String infos = "§7-/-";
                String name1 = w.getName();
                String difficulty = w.getDifficulty().name();
                if (config.get("worlds." + w.getName() + ".info") != null) {
                    infos = config.getString("worlds." + w.getName() + ".info", "§7-/-");
                    worldtype = config.getString("worlds." + w.getName() + ".environment");
                    name1 = config.getString("worlds." + w.getName() + ".name", w.getName());
                    difficulty = config.getString("worlds." + w.getName() + ".difficulty");
                }
                infoLore.add(I18n.as("worldmanage.gui.lore0") + name1.replace("&", "§"));
                infoLore.add(I18n.as("worldmanage.gui.lore1") + infos.replace("&", "§"));
                infoLore.add(I18n.as("worldmanage.gui.lore2") + w.getWorldBorder().getSize());
                infoLore.add(I18n.as("worldmanage.gui.lore3") + worldtype);
                infoLore.add(I18n.as("worldmanage.gui.lore4") + difficulty);
                if (w.isMods()) {
                    infoLore.add("§bModid §8>> §7" + w.getModid());
                }
                if (w.isBukkit()) {
                    infoLore.add("§bPluginWorld §8>> §7" + w.isBukkit());
                }
            }
            inv.setItem(pos, ItemAPI.doItem(Material.MAP, 1, "§7>> §6" + w.getName(), infoLore));
            ++pos;
            infoLore.clear();
        }
        inv.setItem(53, ItemAPI.doItem(Material.REDSTONE_BLOCK, 1, I18n.as("worldmanage.gui.close"), null));
        p.openInventory(inv);
        InventoryClickListener.worldInventory = worldListInventory;
    }
}
