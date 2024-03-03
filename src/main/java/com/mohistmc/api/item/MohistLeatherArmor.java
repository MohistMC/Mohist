package com.mohistmc.api.item;

import java.util.Random;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class MohistLeatherArmor {

    private LeatherArmorMeta lm;
    private ItemStack item;

    public static MohistLeatherArmor create(ItemStack armor) {
        return new MohistLeatherArmor(armor);
    }

    public MohistLeatherArmor(ItemStack armor) {
        this.item = armor;
        this.lm = (LeatherArmorMeta)this.item.getItemMeta();
    }

    public MohistLeatherArmor setColor(Color color) {
        this.lm.setColor(color);
        return this;
    }

    public MohistLeatherArmor setRandomColor() {
        this.lm.setColor(Color.fromRGB(this.randomColor(255) + 1, this.randomColor(255) + 1, this.randomColor(255) + 1));
        return this;
    }

    private int randomColor(int max) {
        Random r = new Random();
        return r.nextInt(max);
    }

    public MohistLeatherArmor clone(ItemStack armor) {
        this.item = armor;
        this.lm = (LeatherArmorMeta)armor.getItemMeta();
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.lm);
        return this.item;
    }
}
