package org.bukkit.craftbukkit.v1_18_R2.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.bukkit.block.Lectern;
import org.bukkit.inventory.LecternInventory;

public class CraftInventoryLectern extends CraftInventory implements LecternInventory {

    public net.minecraft.world.MenuProvider tile;

    public CraftInventoryLectern(Container inventory) {
        super(inventory);
        if (inventory instanceof LecternBlockEntity.LecternInventory) {
            this.tile = ((LecternBlockEntity.LecternInventory) inventory).getLectern();
        }
    }

    @Override
    public Lectern getHolder() {
        return (Lectern) inventory.getOwner();
    }
}
