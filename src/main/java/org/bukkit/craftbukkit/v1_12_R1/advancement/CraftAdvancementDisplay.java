package org.bukkit.craftbukkit.v1_12_R1.advancement;

import net.minecraft.advancements.DisplayInfo;
import org.bukkit.advancement.AdvancementDisplay;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftChatMessage;

public class CraftAdvancementDisplay implements org.bukkit.advancement.AdvancementDisplay {
    private final DisplayInfo handle;

    public CraftAdvancementDisplay(DisplayInfo handle) {
        this.handle = handle;
    }

    public DisplayInfo getHandle() {
        return handle;
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(handle.getTitle());
    }

    @Override
    public String getDescription() {
        return CraftChatMessage.fromComponent(handle.getDescription());
    }

    @Override
    public AdvancementDisplay getFrameType() {
        return handle.bukkit;
    }

    @Override
    public boolean shouldAnnounceToChat() {
        return handle.shouldAnnounceToChat();
    }

    @Override
    public void setShouldAnnounceToChat(boolean announce) {
        handle.setShouldAnnounceToChat(announce);
    }

    @Override
    public boolean isHidden() {
        return handle.isHidden();
    }
}