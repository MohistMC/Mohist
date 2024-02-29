package com.mohistmc.plugins.world.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class WorldListInventory implements InventoryHolder {

    private final Inventory inventory;

    public WorldListInventory(int size, String title) {
        this.inventory = Bukkit.createInventory(this, size, title);
    }
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
