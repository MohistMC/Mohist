package org.bukkit.craftbukkit.v1_20_R1.advancement;

import com.mohistmc.paper.advancement.AdvancementDisplay;
import com.mohistmc.paper.adventure.PaperAdventure;
import net.minecraft.advancements.Advancement;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;

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

    // Paper start
    @Override
    public AdvancementDisplay getDisplay() {
        return this.handle.getDisplay() == null ? null : this.handle.getDisplay().paper;
    }

    public org.bukkit.advancement.AdvancementDisplay getDisplay0() { // May be called by plugins via Commodore
        return this.handle.getDisplay() == null ? null : new CraftAdvancementDisplay(this.handle.getDisplay());
    }

    @Override
    public net.kyori.adventure.text.Component displayName() {
        return PaperAdventure.asAdventure(Advancement.constructDisplayComponent(this.handle.getId(), this.handle.getDisplay()));
    }

    @Override
    public org.bukkit.advancement.Advancement getParent() {
        return this.handle.getParent() == null ? null : this.handle.getParent().bukkit;
    }

    @Override
    public Collection<org.bukkit.advancement.Advancement> getChildren() {
        final var children = com.google.common.collect.ImmutableList.<org.bukkit.advancement.Advancement>builder();
        for (Advancement advancement : this.handle.getChildren()) {
            children.add(advancement.bukkit);
        }
        return children.build();
    }

    @Override
    public org.bukkit.advancement.Advancement getRoot() {
        Advancement advancement = this.handle;
        while (advancement.getParent() != null) {
            advancement = advancement.getParent();
        }
        return advancement.bukkit;
    }
    // Paper end
}
