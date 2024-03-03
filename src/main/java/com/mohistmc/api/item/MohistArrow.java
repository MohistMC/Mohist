package com.mohistmc.api.item;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MohistArrow {

    private final ItemStack item;
    private final PotionMeta pm;

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

    public MohistArrow buildItemMeta() {
        this.item.setItemMeta(this.pm);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}
