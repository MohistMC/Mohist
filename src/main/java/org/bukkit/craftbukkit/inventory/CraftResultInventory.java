package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.ItemStack;

public class CraftResultInventory extends CraftInventory {

    private final IInventory resultInventory;

    public CraftResultInventory(IInventory inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getIngredientsInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < getIngredientsInventory().getSizeInventory()) {
            net.minecraft.item.ItemStack item = getIngredientsInventory().getStackInSlot(slot);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = getResultInventory().getStackInSlot(slot - getIngredientsInventory().getSizeInventory());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getIngredientsInventory().getSizeInventory()) {
            getIngredientsInventory().setInventorySlotContents(index, CraftItemStack.asNMSCopy(item));
        } else {
            getResultInventory().setInventorySlotContents((index - getIngredientsInventory().getSizeInventory()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public int getSize() {
        return getResultInventory().getSizeInventory() + getIngredientsInventory().getSizeInventory();
    }
}
