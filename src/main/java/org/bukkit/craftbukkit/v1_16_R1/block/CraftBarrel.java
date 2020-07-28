package org.bukkit.craftbukkit.v1_16_R1.block;

import net.minecraft.block.entity.BarrelBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftBarrel extends CraftLootable<BarrelBlockEntity> implements Barrel {

    public CraftBarrel(Block block) {
        super(block, BarrelBlockEntity.class);
    }

    public CraftBarrel(Material material, BarrelBlockEntity te) {
        super(material, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced())
            return this.getSnapshotInventory();

        return new CraftInventory(getTileEntity());
    }

}
