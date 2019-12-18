package org.bukkit.inventory;

import org.bukkit.block.Furnace;

/**
 * Interface to the inventory of a Furnace.
 */
public interface FurnaceInventory extends Inventory {

    /**
     * Get the current item in the result slot.
     *
     * @return The item
     */
    ItemStack getResult();

    /**
     * Set the current item in the result slot.
     *
     * @param stack The item
     */
    void setResult(ItemStack stack);

    /**
     * Get the current fuel.
     *
     * @return The item
     */
    ItemStack getFuel();

    /**
     * Set the current fuel.
     *
     * @param stack The item
     */
    void setFuel(ItemStack stack);

    /**
     * Get the item currently smelting.
     *
     * @return The item
     */
    ItemStack getSmelting();

    /**
     * Set the item currently smelting.
     *
     * @param stack The item
     */
    void setSmelting(ItemStack stack);

    Furnace getHolder();
}
