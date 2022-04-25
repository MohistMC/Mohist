package org.bukkit.craftbukkit.v1_18_R2.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaArmorStand extends CraftMetaItem {

    static final ItemMetaKey ENTITY_TAG = new ItemMetaKey("EntityTag", "entity-tag");
    CompoundTag entityTag;

    CraftMetaArmorStand(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaArmorStand)) {
            return;
        }

        CraftMetaArmorStand armorStand = (CraftMetaArmorStand) meta;
        this.entityTag = armorStand.entityTag;
    }

    CraftMetaArmorStand(CompoundTag tag) {
        super(tag);

        if (tag.contains(ENTITY_TAG.NBT)) {
            entityTag = tag.getCompound(ENTITY_TAG.NBT).copy();
        }
    }

    CraftMetaArmorStand(Map<String, Object> map) {
        super(map);
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(ENTITY_TAG.NBT)) {
            entityTag = tag.getCompound(ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(Map<String, Tag> internalTags) {
        if (entityTag != null && !entityTag.isEmpty()) {
            internalTags.put(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    void applyToItem(CompoundTag tag) {
        super.applyToItem(tag);

        if (entityTag != null) {
            tag.put(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case ARMOR_STAND:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isArmorStandEmpty();
    }

    boolean isArmorStandEmpty() {
        return !(entityTag != null);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaArmorStand) {
            CraftMetaArmorStand that = (CraftMetaArmorStand) meta;

            return entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : entityTag == null;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmorStand || isArmorStandEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (entityTag != null) {
            hash = 73 * hash + entityTag.hashCode();
        }

        return original != hash ? CraftMetaArmorStand.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        return builder;
    }

    @Override
    public CraftMetaArmorStand clone() {
        CraftMetaArmorStand clone = (CraftMetaArmorStand) super.clone();

        if (entityTag != null) {
            clone.entityTag = entityTag.copy();
        }

        return clone;
    }
}
