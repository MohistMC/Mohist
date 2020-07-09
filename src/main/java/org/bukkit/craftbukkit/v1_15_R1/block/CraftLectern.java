package org.bukkit.craftbukkit.v1_15_R1.block;

import net.minecraft.block.LecternBlock;
import net.minecraft.tileentity.LecternTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryLectern;
import org.bukkit.inventory.Inventory;

public class CraftLectern extends CraftBlockEntityState<LecternTileEntity> implements Lectern {

    public CraftLectern(Block block) {
        super(block, LecternTileEntity.class);
    }

    public CraftLectern(Material material, LecternTileEntity te) {
        super(material, te);
    }

    @Override
    public int getPage() {
        return getSnapshot().getPage();
    }

    @Override
    public void setPage(int page) {
        getSnapshot().setPage(page);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventoryLectern(this.getSnapshot().inventory);
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryLectern(this.getTileEntity().inventory);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.LECTERN) {
            LecternBlock.pulse(this.world.getHandle(), this.getPosition(), this.getHandle());
        }

        return result;
    }
}
