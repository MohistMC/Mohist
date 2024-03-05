package com.mohistmc.api.gui;

import org.bukkit.inventory.ItemStack;

public class GUIItem {
    private final ItemStack display;
    private final ClickAction clickAction;

    public GUIItem(ItemStack display) {
        this.display = display;
        this.clickAction = null;
    }

    public GUIItem(ItemStack display, ClickAction clickAction) {
        this.display = display;
        this.clickAction = clickAction;
    }

    public ItemStack display() {
        return display;
    }

    public ClickAction clickAction() {
        return clickAction;
    }
}
