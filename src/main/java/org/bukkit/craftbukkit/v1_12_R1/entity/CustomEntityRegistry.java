package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.registry.EntityEntry;

import javax.annotation.Nullable;
import java.util.Iterator;

public class CustomEntityRegistry<K, V> extends RegistryNamespaced<K, V> {
    public void register(int id, K key, V value) {
        net.minecraftforge.registries.GameData.registerEntity(id, (ResourceLocation) key, (Class<? extends Entity>) value, ((ResourceLocation) key).getResourcePath());
    }

    @Nullable
    public V getObject(@Nullable K name) {
        EntityEntry entry = net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES.getValue((ResourceLocation) name);
        return entry == null ? null : (V) entry.getEntityClass();
    }

    @Nullable
    public K getNameForObject(V value) {
        EntityEntry entry = net.minecraftforge.fml.common.registry.EntityRegistry.getEntry((Class<? extends Entity>) value);
        return entry == null ? null : (K) entry.getRegistryName();
    }

    public boolean containsKey(K key) {
        return net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES.getValue((ResourceLocation) key) != null;
    }

    public int getIDForObject(@Nullable V value) {
        EntityEntry entry = net.minecraftforge.fml.common.registry.EntityRegistry.getEntry((Class<? extends Entity>) value);
        return entry == null ? -1 : net.minecraftforge.registries.GameData.getEntityRegistry().getID(entry);
    }

    @Nullable
    public V getObjectById(int id) {
        EntityEntry entry = net.minecraftforge.registries.GameData.getEntityRegistry().getValue(id);
        return entry == null ? null : (V) entry;
    }

    public Iterator<V> iterator() {
        return (Iterator<V>) net.minecraftforge.registries.GameData.getEntityRegistry().getValues().iterator();
    }
}
