package org.bukkit.advancement;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum FrameType {
    TASK(ChatColor.GREEN),
    CHALLENGE(ChatColor.DARK_PURPLE),
    GOAL(ChatColor.GREEN);

    private final ChatColor color;

    FrameType(ChatColor color) {
        this.color = color;
    }

    @NotNull
    public ChatColor getColor() {
        return color;
    }

    @NotNull
    @Override
    public String toString() {
        return "FrameType[name=" + name() + ",color=" + color + "]";
    }
}