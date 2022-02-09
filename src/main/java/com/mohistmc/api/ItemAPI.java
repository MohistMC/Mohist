package com.mohistmc.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.util.ResourceLocation;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemAPI {

    public static Map<String, String> MODNAME_MAP = new ConcurrentHashMap();
    public static Map<Integer, String> MODID_MAP = new ConcurrentHashMap();
    public static List<ResourceLocation> vanilla_item = new ArrayList<>();
    public static Map<Integer, Integer> ITEM_BLOCK = new ConcurrentHashMap();

    public static net.minecraft.item.ItemStack toNMSItem(Material materialcb) {
        ItemStack itemStackcb = new ItemStack(materialcb);
        return CraftItemStack.asNMSCopy(itemStackcb);
    }

    public static ItemStack getBukkit(Material material) {
        return CraftItemStack.asBukkitCopy(toNMSItem(material));
    }

    public static ItemStack doItem(Material material, int menge, int sid, String name, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material, menge, (short)sid);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static String getModid(String name) {
        return MODNAME_MAP.getOrDefault(name, "unknown");
    }

    public static String getModid(int id) {
        return MODNAME_MAP.getOrDefault(id, "unknown");
    }
}
