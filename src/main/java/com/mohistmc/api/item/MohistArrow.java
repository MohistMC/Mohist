package com.mohistmc.api.item;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MohistArrow {

    private ItemStack item;
    private PotionMeta pm;

    public static MohistArrow create(ItemStack arrow) {
        return new MohistArrow(arrow);
    }

    public MohistArrow(ItemStack arrow) {
        this.item = arrow;
        this.pm = (PotionMeta)arrow.getItemMeta();
    }

    public MohistArrow setColor(Color color) {
        this.pm.setColor(color);
        return this;
    }

    public MohistArrow addCustomEffect(PotionEffectType type, int dura, int amp) {
        this.pm.addCustomEffect(new PotionEffect(type, dura, amp), true);
        return this;
    }

    public MohistArrow clone(ItemStack arrow) {
        this.item = arrow;
        this.pm = (PotionMeta)item.getItemMeta();
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.pm);
        return this.item;
    }
}
