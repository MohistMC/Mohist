package org.bukkit.craftbukkit.v1_16_R1.inventory;

import net.minecraft.inventory.Inventory;
import org.bukkit.block.Lectern;
import org.bukkit.inventory.LecternInventory;
import red.mohist.extra.inventory.ExtraInventory;

public class CraftInventoryLectern extends CraftInventory implements LecternInventory {

    public CraftInventoryLectern(Inventory inventory) {
        super(inventory);
    }

    @Override
    public Lectern getHolder() {
        return (Lectern) ((ExtraInventory)(Object)inventory).getOwner();
    }

}