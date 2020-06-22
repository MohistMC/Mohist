package org.bukkit.craftbukkit.v1_15_R1.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.LoomInventory;

public class CraftInventoryLoom extends CraftResultInventory implements LoomInventory {

    public CraftInventoryLoom(IInventory inventory, IInventory resultInventory) {
        super(inventory, resultInventory);
    }
}
