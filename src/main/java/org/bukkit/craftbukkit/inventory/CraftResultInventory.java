package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftResultInventory extends CraftInventory {

    private final Inventory resultInventory;

    public CraftResultInventory(Inventory inventory, Inventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public Inventory getResultInventory() {
        return resultInventory;
    }

    public Inventory getIngredientsInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < getIngredientsInventory().getInvSize()) {
            net.minecraft.item.ItemStack item = getIngredientsInventory().getInvStack(slot);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = getResultInventory().getInvStack(slot - getIngredientsInventory().getInvSize());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getIngredientsInventory().getInvSize()) {
            getIngredientsInventory().setInvStack(index, CraftItemStack.asNMSCopy(item));
        } else {
            getResultInventory().setInvStack((index - getIngredientsInventory().getInvSize()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public int getSize() {
        return getResultInventory().getInvSize() + getIngredientsInventory().getInvSize();
    }
}
