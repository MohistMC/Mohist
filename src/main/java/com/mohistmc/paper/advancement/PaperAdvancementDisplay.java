package com.mohistmc.paper.advancement;

import com.mohistmc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PaperAdvancementDisplay(DisplayInfo handle) implements AdvancementDisplay {

    @Override
    public @NotNull Frame frame() {
        return asPaperFrame(this.handle.getFrame());
    }

    @Override
    public @NotNull Component title() {
        return PaperAdventure.asAdventure(this.handle.getTitle());
    }

    @Override
    public @NotNull Component description() {
        return PaperAdventure.asAdventure(this.handle.getDescription());
    }

    @Override
    public @NotNull ItemStack icon() {
        return CraftItemStack.asBukkitCopy(this.handle.getIcon());
    }

    @Override
    public boolean doesShowToast() {
        return this.handle.shouldShowToast();
    }

    @Override
    public boolean doesAnnounceToChat() {
        return this.handle.shouldAnnounceChat();
    }

    @Override
    public boolean isHidden() {
        return this.handle.isHidden();
    }

    @Override
    public @Nullable NamespacedKey backgroundPath() {
        return this.handle.getBackground() == null ? null : CraftNamespacedKey.fromMinecraft(this.handle.getBackground());
    }

    @Override
    public @NotNull Component displayName() {
        return PaperAdventure.asAdventure(Advancement.constructDisplayComponent(null, this.handle));
    }

    public static @NotNull Frame asPaperFrame(@NotNull FrameType frameType) {
        return switch (frameType) {
            case TASK -> Frame.TASK;
            case CHALLENGE -> Frame.CHALLENGE;
            case GOAL -> Frame.GOAL;
        };
    }
}
