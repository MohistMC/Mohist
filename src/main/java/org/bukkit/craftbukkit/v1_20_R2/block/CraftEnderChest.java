package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.World;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<EnderChestBlockEntity> implements EnderChest {

    public CraftEnderChest(World world, final EnderChestBlockEntity te) {
        super(world, te);
    }

    protected CraftEnderChest(CraftEnderChest state) {
        super(state);
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().openersCounter.opened && getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = getTileEntity().getBlockState();
            int openCount = getTileEntity().openersCounter.getOpenerCount();

            getTileEntity().openersCounter.onAPIOpen((net.minecraft.world.level.Level) getWorldHandle(), getPosition(), block);
            getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) getWorldHandle(), getPosition(), block, openCount, openCount + 1);
        }
        getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().openersCounter.opened && getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = getTileEntity().getBlockState();
            int openCount = getTileEntity().openersCounter.getOpenerCount();

            getTileEntity().openersCounter.onAPIClose((net.minecraft.world.level.Level) getWorldHandle(), getPosition(), block);
            getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) getWorldHandle(), getPosition(), block, openCount, 0);
        }
        getTileEntity().openersCounter.opened = false;
    }

    @Override
    public CraftEnderChest copy() {
        return new CraftEnderChest(this);
    }
}
