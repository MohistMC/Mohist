package org.bukkit.craftbukkit.v1_18_R2.inventory;

import net.minecraft.world.Container;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory {
    public CraftInventoryBrewer(Container inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getIngredient() {
        return getItem(3);
    }

    @Override
    public void setIngredient(ItemStack ingredient) {
        setItem(3, ingredient);
    }

    @Override
    public BrewingStand getHolder() {
        return (BrewingStand) inventory.getOwner();
    }

    @Override
    public ItemStack getFuel() {
        return getItem(4);
    }

    @Override
    public void setFuel(ItemStack fuel) {
        setItem(4, fuel);
    }
}
