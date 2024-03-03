package com.mohistmc.api.item;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MohistPotion
{
    private PotionMeta pm;
    private ItemStack item;

    public MohistPotion(ItemStack potion) {
        this.item = potion;
        this.pm = (PotionMeta)this.item.getItemMeta();
    }

    public MohistPotion setColor(Color color) {
        this.pm.setColor(color);
        return this;
    }

    public MohistPotion addEffect(PotionEffectType type, int dura, int amp) {
        this.pm.addCustomEffect(new PotionEffect(type, dura, amp), true);
        return this;
    }

    public MohistPotion clone(ItemStack potion) {
        this.item = potion;
        this.pm = (PotionMeta)potion.getItemMeta();
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.pm);
        return this.item;
    }
}
