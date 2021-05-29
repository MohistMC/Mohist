package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.LecternTileEntity;
import org.bukkit.block.Lectern;
import org.bukkit.inventory.LecternInventory;

public class CraftInventoryLectern extends CraftInventory implements LecternInventory {

    public INamedContainerProvider tile;

    public CraftInventoryLectern(IInventory inventory) {
        super(inventory);
        if (inventory instanceof LecternTileEntity.LecternInventory) {
            this.tile = ((LecternTileEntity.LecternInventory) inventory).getLectern();
        }
    }

    @Override
    public Lectern getHolder() {
        return (Lectern) inventory.getOwner();
    }
}
