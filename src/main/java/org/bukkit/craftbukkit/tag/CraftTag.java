package org.bukkit.craftbukkit.tag;

import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.Identifier;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import red.mohist.extra.tag.ExtraRegistryTagContainer;

public abstract class CraftTag<N, B extends Keyed> implements Tag<B> {

    private final net.minecraft.tag.RegistryTagContainer<N> registry;
    private final Identifier tag;
    //
    private int version = -1;
    private net.minecraft.tag.Tag<N> handle;

    public CraftTag(RegistryTagContainer<N> registry, Identifier tag) {
        this.registry = registry;
        this.tag = tag;
    }

    protected net.minecraft.tag.Tag<N> getHandle() {
        if (version != ((ExtraRegistryTagContainer) this).getVersion()) {
            handle = registry.getOrCreate(tag);
            version = ((ExtraRegistryTagContainer) this).getVersion();
        }

        return handle;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(tag);
    }
}
