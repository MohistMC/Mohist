package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAnvil extends CraftInventory implements AnvilInventory {
    private final net.minecraft.inventory.IInventory resultInventory;

    public CraftInventoryAnvil(net.minecraft.inventory.IInventory inventory, net.minecraft.inventory.IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public net.minecraft.inventory.IInventory getResultInventory() {
        return resultInventory;
    }

    public net.minecraft.inventory.IInventory getIngredientsInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < getIngredientsInventory().getSizeInventory()) {
            net.minecraft.item.ItemStack item = getIngredientsInventory().getStackInSlot(slot);
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = getResultInventory().getStackInSlot(slot - getIngredientsInventory().getSizeInventory());
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getIngredientsInventory().getSizeInventory()) {
            getIngredientsInventory().setInventorySlotContents(index, (item == null ? null : CraftItemStack.asNMSCopy(item)));
        } else {
            getResultInventory().setInventorySlotContents((index - getIngredientsInventory().getSizeInventory()), (item == null ? null : CraftItemStack.asNMSCopy(item)));
        }
    }

    @Override
    public int getSize() {
        return getResultInventory().getSizeInventory() + getIngredientsInventory().getSizeInventory();
    }
}
