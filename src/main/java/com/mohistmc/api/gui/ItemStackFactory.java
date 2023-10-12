package com.mohistmc.api.gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author LSeng
 */
public class ItemStackFactory {

    ItemStack item;

    private ItemStackFactory() {
    }

    public ItemStackFactory(String type) {
        this(Material.getMaterial(type), 1);
    }

    public ItemStackFactory(Material type) {
        this(type, 1);
    }

    public ItemStackFactory(String type, int amount) {
        this(Material.getMaterial(type), amount);
    }

    public ItemStackFactory(Material type, int amount) {
        this.item = new ItemStack(type, amount);
    }

    public ItemStack toItemStack() {
        return this.item;
    }

    public ItemStackFactory setDisplayName(String name) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(name.replaceAll("&", "§"));
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory setEnchantment(Enchantment enchantment) {
        ItemMeta im = this.item.getItemMeta();
        if (enchantment != null) {
            im.addEnchant(enchantment, 1, false);
        }
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory setLore(List<String> lores) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lores_ = new ArrayList<>();
        for (String lore : lores) {
            lores_.add(lore.replaceAll("&", "§"));
        }
        im.setLore(lores_);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory addLore(String name) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lores;
        if (im.hasLore()) {
            lores = im.getLore();
        } else {
            lores = new ArrayList<>();
        }
        lores.add(name.replaceAll("&", "§"));
        im.setLore(lores);
        this.item.setItemMeta(im);
        return this;
    }

}
