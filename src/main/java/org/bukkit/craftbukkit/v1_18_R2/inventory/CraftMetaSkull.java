package org.bukkit.craftbukkit.v1_18_R2.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_18_R2.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey SKULL_PROFILE = new ItemMetaKey("SkullProfile");

    static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");
    static final int MAX_OWNER_LENGTH = 16;

    private GameProfile profile;
    private CompoundTag serializedProfile;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        CraftMetaSkull skullMeta = (CraftMetaSkull) meta;
        this.setProfile(skullMeta.profile);
    }

    CraftMetaSkull(CompoundTag tag) {
        super(tag);

        if (tag.contains(SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            this.setProfile(NbtUtils.readGameProfile(tag.getCompound(SKULL_OWNER.NBT)));
        } else if (tag.contains(SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_STRING) && !tag.getString(SKULL_OWNER.NBT).isEmpty()) {
            this.setProfile(new GameProfile(null, tag.getString(SKULL_OWNER.NBT)));
        }
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (profile == null) {
            Object object = map.get(SKULL_OWNER.BUKKIT);
            if (object instanceof PlayerProfile) {
                setOwnerProfile((PlayerProfile) object);
            } else {
                setOwner(SerializableMeta.getString(map, SKULL_OWNER.BUKKIT, true));
            }
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(SKULL_PROFILE.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            CompoundTag skullTag = tag.getCompound(SKULL_PROFILE.NBT);
            // convert type of stored Id from String to UUID for backwards compatibility
            if (skullTag.contains("Id", CraftMagicNumbers.NBT.TAG_STRING)) {
                UUID uuid = UUID.fromString(skullTag.getString("Id"));
                skullTag.putUUID("Id", uuid);
            }

            this.setProfile(NbtUtils.readGameProfile(skullTag));
        }
    }

    private void setProfile(GameProfile profile) {
        this.profile = profile;
        this.serializedProfile = (profile == null) ? null : NbtUtils.writeGameProfile(new CompoundTag(), profile);
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        if (profile != null) {
            // SPIGOT-6558: Set initial textures
            tag.put(SKULL_OWNER.NBT, serializedProfile);
            // Fill in textures
            SkullBlockEntity.updateGameprofile(profile, (filledProfile) -> {
                setProfile(filledProfile);
                tag.put(SKULL_OWNER.NBT, serializedProfile);
            });
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
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
                return true;
            default:
                return false;
        }
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
    }

    @Override
    public boolean hasOwner() {
        return profile != null && profile.getName() != null;
    }

    @Override
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

    @Override
    public boolean setOwner(String name) {
        if (name != null && name.length() > MAX_OWNER_LENGTH) {
            return false;
        }

        if (name == null) {
            setProfile(null);
        } else {
            setProfile(new GameProfile(null, name));
        }

        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        if (owner == null) {
            setProfile(null);
        } else if (owner instanceof CraftPlayer) {
            setProfile(((CraftPlayer) owner).getProfile());
        } else {
            setProfile(new GameProfile(owner.getUniqueId(), owner.getName()));
        }

        return true;
    }

    @Override
    public PlayerProfile getOwnerProfile() {
        if (!hasOwner()) {
            return null;
        }

        return new CraftPlayerProfile(profile);
    }

    @Override
    public void setOwnerProfile(PlayerProfile profile) {
        if (profile == null) {
            setProfile(null);
        } else {
            setProfile(CraftPlayerProfile.validateSkullProfile(((CraftPlayerProfile) profile).buildGameProfile()));
        }
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

            // SPIGOT-5403: equals does not check properties
            return (this.profile != null ? that.profile != null && this.serializedProfile.equals(that.serializedProfile) : that.profile == null);
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
        if (this.profile != null) {
            return builder.put(SKULL_OWNER.BUKKIT, new CraftPlayerProfile(this.profile));
        }
        return builder;
    }
}
