package com.mohistmc.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author LSeng
 */
public class GUIItem {

    ItemStack display;

    public GUIItem(ItemStack display) {
        this.display = display;
    }

    public void ClickAction(ClickType type, Player p, ItemStack itemStack) {

    }


}
