package org.bukkit.craftbukkit.v1_18_R2.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftTropicalFish;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaTropicalFishBucket extends CraftMetaItem implements TropicalFishBucketMeta {
    static final ItemMetaKey VARIANT = new ItemMetaKey("BucketVariantTag", "fish-variant");
    static final ItemMetaKey ENTITY_TAG = new ItemMetaKey("EntityTag", "entity-tag");

    private Integer variant;
    private CompoundTag entityTag;

    CraftMetaTropicalFishBucket(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaTropicalFishBucket)) {
            return;
        }

        CraftMetaTropicalFishBucket bucket = (CraftMetaTropicalFishBucket) meta;
        this.variant = bucket.variant;
        this.entityTag = bucket.entityTag;
    }

    CraftMetaTropicalFishBucket(CompoundTag tag) {
        super(tag);

        if (tag.contains(VARIANT.NBT, CraftMagicNumbers.NBT.TAG_INT)) {
            this.variant = tag.getInt(VARIANT.NBT);
        }

        if (tag.contains(ENTITY_TAG.NBT)) {
            entityTag = tag.getCompound(ENTITY_TAG.NBT).copy();
        }
    }

    CraftMetaTropicalFishBucket(Map<String, Object> map) {
        super(map);

        Integer variant = SerializableMeta.getObject(Integer.class, map, VARIANT.BUKKIT, true);
        if (variant != null) {
            this.variant = variant;
        }
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

        if (hasVariant()) {
            tag.putInt(VARIANT.NBT, variant);
        }

        if (entityTag != null) {
            tag.put(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case TROPICAL_FISH_BUCKET:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBucketEmpty();
    }

    boolean isBucketEmpty() {
        return !(hasVariant() || entityTag != null);
    }

    @Override
    public DyeColor getPatternColor() {
        return CraftTropicalFish.getPatternColor(variant);
    }

    @Override
    public void setPatternColor(DyeColor color) {
        if (variant == null) {
            variant = 0;
        }
        variant = CraftTropicalFish.getData(color, getPatternColor(), getPattern());
    }

    @Override
    public DyeColor getBodyColor() {
        return CraftTropicalFish.getBodyColor(variant);
    }

    @Override
    public void setBodyColor(DyeColor color) {
        if (variant == null) {
            variant = 0;
        }
        variant = CraftTropicalFish.getData(getPatternColor(), color, getPattern());
    }

    @Override
    public TropicalFish.Pattern getPattern() {
        return CraftTropicalFish.getPattern(variant);
    }

    @Override
    public void setPattern(TropicalFish.Pattern pattern) {
        if (variant == null) {
            variant = 0;
        }
        variant = CraftTropicalFish.getData(getPatternColor(), getBodyColor(), pattern);
    }

    @Override
    public boolean hasVariant() {
        return variant != null;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaTropicalFishBucket) {
            CraftMetaTropicalFishBucket that = (CraftMetaTropicalFishBucket) meta;

            return (hasVariant() ? that.hasVariant() && this.variant.equals(that.variant) : !that.hasVariant())
                    && (entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : that.entityTag == null);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaTropicalFishBucket || isBucketEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasVariant()) {
            hash = 61 * hash + variant;
        }
        if (entityTag != null) {
            hash = 61 * hash + entityTag.hashCode();
        }

        return original != hash ? CraftMetaTropicalFishBucket.class.hashCode() ^ hash : hash;
    }


    @Override
    public CraftMetaTropicalFishBucket clone() {
        CraftMetaTropicalFishBucket clone = (CraftMetaTropicalFishBucket) super.clone();

        if (entityTag != null) {
            clone.entityTag = entityTag.copy();
        }

        return clone;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasVariant()) {
            builder.put(VARIANT.BUKKIT, variant);
        }

        return builder;
    }
}
