package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.inventory.FurnaceInventory;

/**
 * Represents a captured state of a furnace.
 */
public interface Furnace extends Container, Nameable {

    /**
     * Get burn time.
     *
     * @return Burn time
     */
    short getBurnTime();

    /**
     * Set burn time.
     *
     * @param burnTime Burn time
     */
    void setBurnTime(short burnTime);

    /**
     * Get cook time.
     *
     * @return Cook time
     */
    short getCookTime();

    /**
     * Set cook time.
     *
     * @param cookTime Cook time
     */
    void setCookTime(short cookTime);

    @Override
    FurnaceInventory getInventory();

    @Override
    FurnaceInventory getSnapshotInventory();
}
