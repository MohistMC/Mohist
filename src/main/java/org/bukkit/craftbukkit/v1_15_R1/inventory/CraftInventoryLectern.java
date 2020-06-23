package org.bukkit.craftbukkit.v1_15_R1.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.block.Lectern;
import org.bukkit.inventory.LecternInventory;

public class CraftInventoryLectern extends CraftInventory implements LecternInventory {

    public CraftInventoryLectern(IInventory inventory) {
        super(inventory);
    }

    @Override
    public Lectern getHolder() {
        return (Lectern) inventory.getOwner();
    }
}
