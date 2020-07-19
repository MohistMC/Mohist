package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntitySkull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey SKULL_PROFILE = new ItemMetaKey("SkullProfile");

    static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");
    static final int MAX_OWNER_LENGTH = 16;

    private GameProfile profile;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        CraftMetaSkull skullMeta = (CraftMetaSkull) meta;
        this.profile = skullMeta.profile;
    }

    CraftMetaSkull(NBTTagCompound tag) {
        super(tag);

        if (tag.hasKey(SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            profile = NBTUtil.readGameProfileFromNBT(tag.getCompoundTag(SKULL_OWNER.NBT));
        } else if (tag.hasKey(SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_STRING) && !tag.getString(SKULL_OWNER.NBT).isEmpty()) {
            profile = new GameProfile(null, tag.getString(SKULL_OWNER.NBT));
        }
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (profile == null) {
            setOwner(SerializableMeta.getString(map, SKULL_OWNER.BUKKIT, true));
        }
    }

    @Override
    void deserializeInternal(NBTTagCompound tag) {
        if (tag.hasKey(SKULL_PROFILE.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            profile = NBTUtil.readGameProfileFromNBT(tag.getCompoundTag(SKULL_PROFILE.NBT));
        }
    }

    @Override
    void serializeInternal(final Map<String, NBTBase> internalTags) {
        if (profile != null) {
            NBTTagCompound nbtData = new NBTTagCompound();
            NBTUtil.writeGameProfile(nbtData, profile);
            internalTags.put(SKULL_PROFILE.NBT, nbtData);
        }
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (profile != null) {
            // Fill in textures
            profile = Futures.getUnchecked(TileEntitySkull.updateGameprofile(profile, gameProfile -> true, true)); // Spigot

            NBTTagCompound owner = new NBTTagCompound();
            NBTUtil.writeGameProfile(owner, profile);
            tag.setTag(SKULL_OWNER.NBT, owner);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isSkullEmpty();
    }

    boolean isSkullEmpty() {
        return profile == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case SKULL_ITEM:
                return true;
            default:
                return false;
        }
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
    }

    public boolean hasOwner() {
        return profile != null && profile.getName() != null;
    }

    public String getOwner() {
        return hasOwner() ? profile.getName() : null;
    }

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (hasOwner()) {
            if (profile.getId() != null) {
                return Bukkit.getOfflinePlayer(profile.getId());
            }

            if (profile.getName() != null) {
                return Bukkit.getOfflinePlayer(profile.getName());
            }
        }

        return null;
    }

    public boolean setOwner(String name) {
        if (name != null && name.length() > MAX_OWNER_LENGTH) {
            return false;
        }

        if (name == null) {
            profile = null;
        } else {
            // Paper start - Use Online Players Skull
            GameProfile newProfile = null;
            EntityPlayerMP player = MinecraftServer.getServerInst().getPlayerList().getPlayerByUsername(name);
            if (player != null) {
                newProfile = player.getGameProfile();
            }
            if (newProfile == null) {
                newProfile = new GameProfile(null, name);
            }
            profile = newProfile;
            // Paper end
        }

        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        profile = (owner == null) ? null : new GameProfile(owner.getUniqueId(), owner.getName());

        return true;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasOwner()) {
            hash = 61 * hash + profile.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSkull) {
            CraftMetaSkull that = (CraftMetaSkull) meta;

            return (this.hasOwner() ? that.hasOwner() && this.profile.equals(that.profile) : !that.hasOwner());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || isSkullEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        if (hasOwner()) {
            return builder.put(SKULL_OWNER.BUKKIT, this.profile.getName());
        }
        return builder;
    }
}
