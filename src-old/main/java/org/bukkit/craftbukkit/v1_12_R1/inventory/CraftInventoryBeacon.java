package org.bukkit.craftbukkit.v1_12_R1.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityBeacon;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBeacon extends CraftInventory implements BeaconInventory {
    public CraftInventoryBeacon(TileEntityBeacon beacon) {
        super(beacon);
    }

    public CraftInventoryBeacon(IInventory beacon) {
        super(beacon);
    }

    public ItemStack getItem() {
        return getItem(0);
    }

    public void setItem(ItemStack item) {
        setItem(0, item);
    }
}
