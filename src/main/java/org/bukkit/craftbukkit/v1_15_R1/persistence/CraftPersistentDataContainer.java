package org.bukkit.craftbukkit.v1_15_R1.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftNBTTagConfigSerializer;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class CraftPersistentDataContainer implements PersistentDataContainer {

    private final Map<String, INBT> customDataTagCollection = new HashMap<>();
    private final CraftPersistentDataTypeRegistry registry;
    private final CraftPersistentDataAdapterContext adapterContext;

    public CraftPersistentDataContainer(Map<String, INBT> customTagCollection, CraftPersistentDataTypeRegistry registry) {
        this(registry);
        this.customDataTagCollection.putAll(customTagCollection);
    }

    public CraftPersistentDataContainer(CraftPersistentDataTypeRegistry registry) {
        this.registry = registry;
        this.adapterContext = new CraftPersistentDataAdapterContext(this.registry);
    }

    @Override
    public <T, Z> void set(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        Validate.notNull(key, "The provided key for the custom value was null");
        Validate.notNull(type, "The provided type for the custom value was null");
        Validate.notNull(value, "The provided value for the custom value was null");

        this.customDataTagCollection.put(key.toString(), registry.wrap(type.getPrimitiveType(), type.toPrimitive(value, adapterContext)));
    }

    @Override
    public <T, Z> boolean has(NamespacedKey key, PersistentDataType<T, Z> type) {
        Validate.notNull(key, "The provided key for the custom value was null");
        Validate.notNull(type, "The provided type for the custom value was null");

        INBT value = this.customDataTagCollection.get(key.toString());
        if (value == null) {
            return false;
        }

        return registry.isInstanceOf(type.getPrimitiveType(), value);
    }

    @Override
    public <T, Z> Z get(NamespacedKey key, PersistentDataType<T, Z> type) {
        Validate.notNull(key, "The provided key for the custom value was null");
        Validate.notNull(type, "The provided type for the custom value was null");

        INBT value = this.customDataTagCollection.get(key.toString());
        if (value == null) {
            return null;
        }

        return type.fromPrimitive(registry.extract(type.getPrimitiveType(), value), adapterContext);
    }

    @Override
    public <T, Z> Z getOrDefault(NamespacedKey key, PersistentDataType<T, Z> type, Z defaultValue) {
        Z z = get(key, type);
        return z != null ? z : defaultValue;
    }

    @Override
    public void remove(NamespacedKey key) {
        Validate.notNull(key, "The provided key for the custom value was null");

        this.customDataTagCollection.remove(key.toString());
    }

    @Override
    public boolean isEmpty() {
        return this.customDataTagCollection.isEmpty();
    }

    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return this.adapterContext;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CraftPersistentDataContainer)) {
            return false;
        }

        Map<String, INBT> myRawMap = getRaw();
        Map<String, INBT> theirRawMap = ((CraftPersistentDataContainer) obj).getRaw();

        return Objects.equals(myRawMap, theirRawMap);
    }

    public CompoundNBT toTagCompound() {
        CompoundNBT tag = new CompoundNBT();
        for (Entry<String, INBT> entry : this.customDataTagCollection.entrySet()) {
            tag.put(entry.getKey(), entry.getValue());
        }
        return tag;
    }

    public void put(String key, INBT base) {
        this.customDataTagCollection.put(key, base);
    }

    public void putAll(Map<String, INBT> map) {
        this.customDataTagCollection.putAll(map);
    }

    public void putAll(CompoundNBT compound) {
        for (String key : compound.keySet()) {
            this.customDataTagCollection.put(key, compound.get(key));
        }
    }

    public Map<String, INBT> getRaw() {
        return this.customDataTagCollection;
    }

    public CraftPersistentDataTypeRegistry getDataTagTypeRegistry() {
        return registry;
    }

    @Override
    public int hashCode() {
        int hashCode = 3;
        hashCode += this.customDataTagCollection.hashCode(); // We will simply add the maps hashcode
        return hashCode;
    }

    public Map<String, Object> serialize() {
        return (Map<String, Object>) CraftNBTTagConfigSerializer.serialize(toTagCompound());
    }
}
