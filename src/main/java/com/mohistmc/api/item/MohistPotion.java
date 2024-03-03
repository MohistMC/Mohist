package com.mohistmc.api.item;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MohistPotion
{
    private final PotionMeta pm;
    private final ItemStack item;

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

    public MohistPotion buildItemMeta() {
        this.item.setItemMeta(this.pm);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}
