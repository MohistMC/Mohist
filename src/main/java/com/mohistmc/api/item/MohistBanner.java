package com.mohistmc.api.item;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class MohistBanner {

    private BannerMeta bm;
    private ItemStack item;

    public static MohistBanner create(ItemStack banner) {
        return new MohistBanner(banner);
    }

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

    public MohistBanner clone(ItemStack banner) {
        this.item = banner;
        this.bm = (BannerMeta)item.getItemMeta();
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.bm);
        return this.item;
    }
}
