package com.mohistmc.plugins.world.utils;

import com.mohistmc.api.ItemAPI;
import com.mohistmc.plugins.MessageI18N;
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
        Inventory inv = Bukkit.createInventory(null, groesse, name);
        for (World w : Bukkit.getWorlds()) {
            ArrayList<String> infoLore = new ArrayList<>();
            FileConfiguration config = ConfigByWorlds.config;
            if (ConfigByWorlds.f.exists() && config.getConfigurationSection("worlds.") != null) {
                String worldtype = w.getEnvironment() == null ? "null" : w.getEnvironment().name();
                String infos = "§7-/-";
                String name1 = w.getName();
                String difficulty = w.getDifficulty().name();
                if (config.get("worlds." + w.getName() + ".info") != null) {
                    infos = config.getString("worlds." + w.getName() + ".info");
                    worldtype = config.getString("worlds." + w.getName() + ".environment");
                    name1 = config.getString("worlds." + w.getName() + ".name");
                    difficulty = config.getString("worlds." + w.getName() + ".difficulty");
                }
                infoLore.add(MessageI18N.WORLDMANAGE_GUI_LORE_0.getKey() + name1.replace("&", "§"));
                infoLore.add(MessageI18N.WORLDMANAGE_GUI_LORE_1.getKey() + infos.replace("&", "§"));
                infoLore.add(MessageI18N.WORLDMANAGE_GUI_LORE_2.getKey() + w.getWorldBorder().getSize());
                infoLore.add(MessageI18N.WORLDMANAGE_GUI_LORE_3.getKey() + worldtype);
                infoLore.add(MessageI18N.WORLDMANAGE_GUI_LORE_4.getKey() + difficulty);
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
        inv.setItem(53, ItemAPI.doItem(Material.REDSTONE_BLOCK, 1, MessageI18N.WORLDMANAGE_GUI_CLOSE.getKey(), null));
        p.openInventory(inv);
    }
}
