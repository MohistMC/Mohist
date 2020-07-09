package org.bukkit.craftbukkit.v1_15_R1.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAbstractHorse extends CraftInventory implements AbstractHorseInventory {

    public CraftInventoryAbstractHorse(IInventory inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getSaddle() {
        return getItem(0);
    }

    @Override
    public void setSaddle(ItemStack stack) {
        setItem(0, stack);
    }
}
