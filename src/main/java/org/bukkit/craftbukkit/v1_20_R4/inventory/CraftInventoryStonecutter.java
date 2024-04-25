package org.bukkit.craftbukkit.v1_20_R4.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.StonecutterInventory;

public class CraftInventoryStonecutter extends CraftResultInventory implements StonecutterInventory {

    public CraftInventoryStonecutter(Container inventory, Container resultInventory) {
        super(inventory, resultInventory);
    }
}
