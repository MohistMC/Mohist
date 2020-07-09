package org.bukkit.craftbukkit.v1_15_R1.block;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryFurnace;
import org.bukkit.inventory.FurnaceInventory;

public class CraftFurnace<T extends AbstractFurnaceTileEntity> extends CraftContainer<T> implements Furnace {

    public CraftFurnace(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftFurnace(final Material material, final T te) {
        super(material, te);
    }

    @Override
    public FurnaceInventory getSnapshotInventory() {
        return new CraftInventoryFurnace(this.getSnapshot());
    }

    @Override
    public FurnaceInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryFurnace(this.getTileEntity());
    }

    @Override
    public short getBurnTime() {
        return (short) this.getSnapshot().burnTime;
    }

    @Override
    public void setBurnTime(short burnTime) {
        this.getSnapshot().burnTime = burnTime;
        // SPIGOT-844: Allow lighting and relighting using this API
        this.data = this.data.with(AbstractFurnaceBlock.LIT, burnTime > 0);
    }

    @Override
    public short getCookTime() {
        return (short) this.getSnapshot().cookTime;
    }

    @Override
    public void setCookTime(short cookTime) {
        this.getSnapshot().cookTime = cookTime;
    }

    @Override
    public int getCookTimeTotal() {
        return this.getSnapshot().cookTimeTotal;
    }

    @Override
    public void setCookTimeTotal(int cookTimeTotal) {
        this.getSnapshot().cookTimeTotal = cookTimeTotal;
    }
}
