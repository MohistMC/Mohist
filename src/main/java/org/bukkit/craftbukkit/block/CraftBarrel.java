package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.BarrelTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftBarrel extends CraftLootable<BarrelTileEntity> implements Barrel {

    public CraftBarrel(Block block) {
        super(block, BarrelTileEntity.class);
    }

    public CraftBarrel(Material material, BarrelTileEntity te) {
        super(material, te);
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
