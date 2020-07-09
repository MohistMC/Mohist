package org.bukkit.craftbukkit.v1_15_R1.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryEnchanting extends CraftInventory implements EnchantingInventory {
    public CraftInventoryEnchanting(IInventory inventory) {
        super(inventory);
    }

    @Override
    public void setItem(ItemStack item) {
        setItem(0,item);
    }

    @Override
    public ItemStack getItem() {
        return getItem(0);
    }

    @Override
    public void setSecondary(ItemStack item) {
        setItem(1, item);
    }

    @Override
    public ItemStack getSecondary() {
        return getItem(1);
    }
}
