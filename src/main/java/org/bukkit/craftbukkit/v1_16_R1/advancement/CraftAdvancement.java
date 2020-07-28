package org.bukkit.craftbukkit.v1_16_R1.advancement;

import java.util.Collection;
import java.util.Collections;
import net.minecraft.advancement.Advancement;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftNamespacedKey;

public class CraftAdvancement implements org.bukkit.advancement.Advancement {

    private final Advancement handle;

    public CraftAdvancement(Advancement handle) {
        this.handle = handle;
    }

    public Advancement getHandle() {
        return this.handle;
    }

    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(this.handle.getId());
    }

    public Collection<String> getCriteria() {
        return Collections.unmodifiableCollection(this.handle.getCriteria().keySet());
    }
}
