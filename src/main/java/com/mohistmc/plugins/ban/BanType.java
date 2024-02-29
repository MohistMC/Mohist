package com.mohistmc.plugins.ban;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 15:10:13
 */
public enum BanType {

    ITEM("ban.item.list"),
    ENTITY("ban.entity.list"),
    ENCHANTMENT("ban.enchantment.list");

    public final String key;

    BanType(String key) {
        this.key = key;
    }
}
