package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.Map;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaSpawnEgg extends CraftMetaItem implements SpawnEggMeta {

    static final ItemMetaKey ENTITY_TAG = new ItemMetaKey("EntityTag", "entity-tag");
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey ENTITY_ID = new ItemMetaKey("id");

    private EntityType spawnedType;
    private NBTTagCompound entityTag;

    CraftMetaSpawnEgg(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaSpawnEgg)) {
            return;
        }

        CraftMetaSpawnEgg egg = (CraftMetaSpawnEgg) meta;
        this.spawnedType = egg.spawnedType;
    }

    CraftMetaSpawnEgg(NBTTagCompound tag) {
        super(tag);

        if (tag.hasKey(ENTITY_TAG.NBT)) {
            entityTag = tag.getCompoundTag(ENTITY_TAG.NBT);

            if (entityTag.hasKey(ENTITY_ID.NBT)) {
                this.spawnedType = EntityType.fromName(new ResourceLocation(entityTag.getString(ENTITY_ID.NBT)).getResourcePath());
            }
        }
    }

    CraftMetaSpawnEgg(Map<String, Object> map) {
        super(map);

        String entityType = SerializableMeta.getString(map, ENTITY_ID.BUKKIT, true);
        setSpawnedType(EntityType.fromName(entityType));
    }

    @Override
    void deserializeInternal(NBTTagCompound tag) {
        super.deserializeInternal(tag);

        if (tag.hasKey(ENTITY_TAG.NBT)) {
            entityTag = tag.getCompoundTag(ENTITY_TAG.NBT);
            MinecraftServer.getServerInst().getDataFixer().process(FixTypes.ENTITY, entityTag); // PAIL: convert TODO: identify DataConverterTypes after implementation

            if (entityTag.hasKey(ENTITY_ID.NBT)) {
                this.spawnedType = EntityType.fromName(new ResourceLocation(entityTag.getString(ENTITY_ID.NBT)).getResourcePath());
            }
        }
    }

    @Override
    void serializeInternal(Map<String, NBTBase> internalTags) {
        if (entityTag != null) {
            internalTags.put(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (!isSpawnEggEmpty() && entityTag == null) {
            entityTag = new NBTTagCompound();
        }

        if (hasSpawnedType()) {
            String[] domain_and_name = this.entityTag.getString("id").split(":");
            entityTag.setString(ENTITY_ID.NBT, new ResourceLocation(domain_and_name[0], spawnedType.getName()).toString().toLowerCase());
        }

        if (entityTag != null) {
            tag.setTag(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case MONSTER_EGG:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isSpawnEggEmpty();
    }

    boolean isSpawnEggEmpty() {
        return !(hasSpawnedType() || entityTag != null);
    }

    boolean hasSpawnedType() {
        return spawnedType != null;
    }

    @Override
    public EntityType getSpawnedType() {
        return spawnedType;
    }

    @Override
    public void setSpawnedType(EntityType type) {
        Preconditions.checkArgument(type == null || type.getName() != null, "Spawn egg type must have name (%s)", type);

        this.spawnedType = type;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSpawnEgg) {
            CraftMetaSpawnEgg that = (CraftMetaSpawnEgg) meta;

            return hasSpawnedType() ? that.hasSpawnedType() && this.spawnedType.equals(that.spawnedType) : !that.hasSpawnedType()
                    && entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : entityTag == null;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSpawnEgg || isSpawnEggEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasSpawnedType()) {
            hash = 73 * hash + spawnedType.hashCode();
        }
        if (entityTag != null) {
            hash = 73 * hash + entityTag.hashCode();
        }

        return original != hash ? CraftMetaSpawnEgg.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasSpawnedType()) {
            builder.put(ENTITY_ID.BUKKIT, spawnedType.getName());
        }

        return builder;
    }

    @Override
    public CraftMetaSpawnEgg clone() {
        CraftMetaSpawnEgg clone = (CraftMetaSpawnEgg) super.clone();

        clone.spawnedType = spawnedType;
        if (entityTag != null) {
            clone.entityTag = entityTag.copy();
        }

        return clone;
    }
}
