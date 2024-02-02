package com.mohistmc.plugins.ban.utils;

import com.mohistmc.plugins.ban.BanType;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class BanSaveInventory implements InventoryHolder {

    private final Inventory inventory;
    private final BanType banType;

    public BanSaveInventory(BanType banType, String title) {
        this.inventory = Bukkit.createInventory(this, 54, title);
        this.banType = banType;
    }
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public BanType getBanType() {
        return banType;
    }
}
