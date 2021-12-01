package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper extends CraftLootable<HopperBlockEntity> implements Hopper {

    public CraftHopper(World world, final HopperBlockEntity te) {
        super(world, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }
}
