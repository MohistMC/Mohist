package com.mohistmc.plugins.item;

import com.mohistmc.util.YamlUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/2 18:27:05
 */
public class ItemsConfig {

    public static File config = new File("mohist-config", "items.yml");
    public static FileConfiguration yaml = YamlConfiguration.loadConfiguration(config);

    public static void init() {
        if (!config.exists()) {
            yaml.set("items", new ArrayList<>());
            save();
        }
    }

    public static void save() {
        YamlUtils.save(config, yaml);
    }

    public static List<ItemStack> getItems(){
        List<ItemStack> list = new ArrayList<>();
        if (yaml.get("items") == null)  return list;
        for (String s : yaml.getConfigurationSection("items").getKeys(false)) {
            list.add(yaml.getItemStack("items." + s));
        }
        return list;
    }

    public static ItemStack get(String item_name) {
        return yaml.getItemStack("items." + item_name, new ItemStack(Material.AIR));
    }

    public static void remove(String iten_name) {
        ItemsConfig.yaml.set("items." + iten_name, null);
        save();
    }
}
