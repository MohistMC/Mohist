package org.bukkit.craftbukkit.v1_18_R2.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.ItemStack;

public class CraftResultInventory extends CraftInventory {

    private final Container resultInventory;

    public CraftResultInventory(Container inventory, Container resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public Container getResultInventory() {
        return resultInventory;
    }

    public Container getIngredientsInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < getIngredientsInventory().getContainerSize()) {
            net.minecraft.world.item.ItemStack item = getIngredientsInventory().getItem(slot);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.world.item.ItemStack item = getResultInventory().getItem(slot - getIngredientsInventory().getContainerSize());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getIngredientsInventory().getContainerSize()) {
            getIngredientsInventory().setItem(index, CraftItemStack.asNMSCopy(item));
        } else {
            getResultInventory().setItem((index - getIngredientsInventory().getContainerSize()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public int getSize() {
        return getResultInventory().getContainerSize() + getIngredientsInventory().getContainerSize();
    }
}
