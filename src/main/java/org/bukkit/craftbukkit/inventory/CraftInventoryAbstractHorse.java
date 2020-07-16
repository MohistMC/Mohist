package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.Inventory;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAbstractHorse extends CraftInventory implements AbstractHorseInventory {

    public CraftInventoryAbstractHorse(Inventory inventory) {
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
