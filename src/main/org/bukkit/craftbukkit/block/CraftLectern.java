package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.inventory.CraftInventoryLectern;
import org.bukkit.inventory.Inventory;

public class CraftLectern extends CraftBlockEntityState<LecternBlockEntity> implements Lectern {

    public CraftLectern(Block block) {
        super(block, LecternBlockEntity.class);
    }

    public CraftLectern(Material material, LecternBlockEntity te) {
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
        return new CraftInventoryLectern(this.getSnapshot().bookAccess);
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryLectern(this.getTileEntity().bookAccess);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.LECTERN) {
            LecternBlock.signalPageChange(this.world.getHandle(), this.getPosition(), this.getHandle());
        }

        return result;
    }
}
