package com.mohistmc.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface ClickAction {

    void run(ClickType clickType, Player owner, ItemStack itemStack);
}
