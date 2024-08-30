package com.mohistmc.plugins.item;

import com.mohistmc.plugins.config.MohistPluginConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/2 18:27:05
 */
public class ItemsConfig extends MohistPluginConfig {

    public static ItemsConfig INSTANCE;

    public ItemsConfig(File file) {
        super(file);
    }

    public static void init() {
        INSTANCE = new ItemsConfig(new File("mohist-config", "items.yml"));
    }

    public List<ItemStack> getItems(){
        List<ItemStack> list = new ArrayList<>();
        ConfigurationSection configurationSection = yaml.getConfigurationSection("items");
        if (yaml.get("items") == null || configurationSection == null)  return list;
        for (String s : configurationSection.getKeys(false)) {
            list.add(yaml.getItemStack("items." + s));
        }
        return list;
    }

    public ItemStack get(String item_name) {
        return yaml.getItemStack("items." + item_name, new ItemStack(Material.AIR));
    }

    public void remove(String iten_name) {
        put("items." + iten_name, null);
    }
}
