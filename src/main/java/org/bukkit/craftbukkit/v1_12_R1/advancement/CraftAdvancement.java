package org.bukkit.craftbukkit.v1_12_R1.advancement;

import net.minecraft.advancements.Advancement;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import java.util.Collection;
import java.util.Collections;

public class CraftAdvancement implements org.bukkit.advancement.Advancement {

    private final Advancement handle;

    public CraftAdvancement(Advancement handle) {
        this.handle = handle;
    }

    public Advancement getHandle() {
        return handle;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(handle.getId());
    }

    @Override
    public Collection<String> getCriteria() {
        return Collections.unmodifiableCollection(handle.getCriteria().keySet());
    }

    // Purpur start
    @Override
    public org.bukkit.advancement.AdvancementDisplay getDisplay() {
        return getHandle().getDisplay() == null ? null : getHandle().getDisplay().bukkit;
    }
    // Purpur end
}
