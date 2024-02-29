package com.mohistmc.plugins.world.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class WorldCreateInventory implements InventoryHolder {

    private final Inventory inventory;

    public WorldCreateInventory(String title) {
        this.inventory = Bukkit.createInventory(this, 27, title);
    }
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
