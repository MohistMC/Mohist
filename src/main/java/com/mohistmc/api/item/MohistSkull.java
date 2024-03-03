package com.mohistmc.api.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MohistSkull
{
    private SkullMeta sm;
    private ItemStack item;

    public static MohistSkull create(ItemStack skull) {
        return new MohistSkull(skull);
    }

    public MohistSkull(ItemStack skull) {
        this.item = skull;
        this.sm = (SkullMeta)skull.getItemMeta();
    }

    public MohistSkull setOwnerByName(String owner) {
        this.sm.setOwner(owner);
        return this;
    }

    public MohistSkull setTexture(String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", texture));
        Field profileField = null;
        try {
            profileField = this.sm.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(this.sm, profile);
        }
        catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return this;
    }

    public MohistSkull clone(ItemStack skull) {
        this.item = skull;
        this.sm = (SkullMeta)skull.getItemMeta();
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.sm);
        return this.item;
    }
}
