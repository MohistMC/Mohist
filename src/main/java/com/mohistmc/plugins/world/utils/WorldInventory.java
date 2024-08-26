package com.mohistmc.plugins.world.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class WorldInventory implements InventoryHolder {

    private final Inventory inventory;
    private final WorldInventoryType worldInventoryType;

    public WorldInventory(WorldInventoryType worldInventoryType, int size, String title) {
        this.inventory = Bukkit.createInventory(this, size, title);
        this.worldInventoryType = worldInventoryType;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public WorldInventoryType worldInventoryType() {
        return worldInventoryType;
    }
}
