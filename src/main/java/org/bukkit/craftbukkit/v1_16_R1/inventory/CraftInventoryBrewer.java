package org.bukkit.craftbukkit.v1_16_R1.inventory;

import net.minecraft.inventory.Inventory;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import red.mohist.extra.inventory.ExtraInventory;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory {

    public CraftInventoryBrewer(Inventory inventory) {
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
        return (BrewingStand) ((ExtraInventory)(Object)inventory).getOwner();
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