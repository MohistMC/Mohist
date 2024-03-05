package com.mohistmc.api.item;

import com.mohistmc.api.color.ColorsAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MohistItem {

    private ItemStack item;
    private ItemMeta itemMeta;

    public static MohistItem create(Material mat) {
        return new MohistItem(mat);
    }

    public MohistItem(Material mat) {
        item = new ItemStack(mat);
        itemMeta = item.getItemMeta();
    }

    public MohistItem setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public MohistItem addEnchantment(Enchantment ench, int level) {
        item.addEnchantment(ench, level);
        return this;
    }

    public MohistItem addEnchantments(Map<Enchantment, Integer> ench) {
        item.addEnchantments(ench);
        return this;
    }

    public MohistItem setDisplayName(String name) {
        itemMeta.setDisplayName(ColorsAPI.of(name));
        return this;
    }

    public MohistItem setDisplayLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public MohistItem setDurability(short durability) {
        item.setDurability(durability);
        return this;
    }

    public MohistItem addDisplayLore(String lore) {
        List<String> lores = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lores.add(ColorsAPI.of(lore));
        itemMeta.setLore(lores);
        return this;
    }

    public MohistItem addFlag(ItemFlag flag) {
        itemMeta.addItemFlags(flag);
        return this;
    }

    public MohistItem setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public MohistItem clone(ItemStack item) {
        this.item = item;
        this.itemMeta = item.getItemMeta();
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(itemMeta);
        return item;
    }
}
