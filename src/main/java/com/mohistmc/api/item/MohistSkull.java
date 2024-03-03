package com.mohistmc.api.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MohistSkull
{
    private final SkullMeta sm;
    private final ItemStack item;

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

    public MohistSkull buildItemMeta() {
        this.item.setItemMeta(this.sm);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}
