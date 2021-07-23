package org.bukkit.craftbukkit.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest extends CraftLootable<ChestBlockEntity> implements Chest {

    public CraftChest(final Block block) {
        super(block, ChestBlockEntity.class);
    }

    public CraftChest(final Material material, final ChestBlockEntity te) {
        super(material, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getBlockInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public Inventory getInventory() {
        CraftInventory inventory = (CraftInventory) this.getBlockInventory();
        if (!isPlaced()) {
            return inventory;
        }

        // The logic here is basically identical to the logic in ChestBlock.interact
        CraftWorld world = (CraftWorld) this.getWorld();

        ChestBlock blockChest= (ChestBlock) (this.getType() == Material.CHEST ? Blocks.CHEST : Blocks.TRAPPED_CHEST);
        MenuProvider nms = blockChest.getMenuProvider(data, world.getHandle(), this.getPosition());

        if (nms instanceof ChestBlock.DoubleInventory) {
            inventory = new CraftInventoryDoubleChest((ChestBlock.DoubleInventory) nms);
        }
        return inventory;
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().openersCounter.opened) {
            BlockState block = getTileEntity().getBlockState();
            getTileEntity().getLevel().blockEvent(getPosition(), block.getBlock(), 1, getTileEntity().openersCounter.getOpenerCount() + 1);
            ChestBlockEntity.playSound(getTileEntity().getLevel(), getPosition(), block, SoundEvents.CHEST_OPEN);
        }
        getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().openersCounter.opened) {
            BlockState block = getTileEntity().getBlockState();
            getTileEntity().getLevel().blockEvent(getPosition(), block.getBlock(), 1, 0);
            ChestBlockEntity.playSound(getTileEntity().getLevel(), getPosition(), block, SoundEvents.CHEST_CLOSE);
        }
        getTileEntity().openersCounter.opened = false;
    }
}
