package org.bukkit.craftbukkit.tag;

import net.minecraft.util.ResourceLocation;
import net.minecraft.tags.NetworkTagCollection;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public abstract class CraftTag<N, B extends Keyed> implements Tag<B> {

    private final net.minecraft.tags.NetworkTagCollection<N> registry;
    private final ResourceLocation tag;
    //
    private int version = -1;
    private net.minecraft.tags.Tag<N> handle;

    public CraftTag(NetworkTagCollection<N> registry, ResourceLocation tag) {
        this.registry = registry;
        this.tag = tag;
    }

    protected net.minecraft.tags.Tag<N> getHandle() {
        if (version != registry.version) {
            handle = registry.getOrCreate(tag);
            version = registry.version;
        }

        return handle;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(tag);
    }
}
