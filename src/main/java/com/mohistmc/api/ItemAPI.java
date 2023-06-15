package com.mohistmc.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemAPI {

    public static ItemStack doItem(Material material, int menge, String name, ArrayList<String> lore) {
        final ItemStack item = new ItemStack(material, menge);
        final ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
