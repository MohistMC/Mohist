package com.mohistmc.api.item;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class MohistBanner {

    private final BannerMeta bm;
    private final ItemStack item;

    public MohistBanner(ItemStack banner) {
        this.item = banner;
        this.bm = (BannerMeta)this.item.getItemMeta();
    }

    public MohistBanner setBasecolor(DyeColor color) {
        this.bm.setBaseColor(color);
        return this;
    }

    public MohistBanner addPattern(DyeColor color, PatternType pattern) {
        this.bm.addPattern(new Pattern(color, pattern));
        return this;
    }

    public MohistBanner buildItemMeta() {
        this.item.setItemMeta(this.bm);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}
