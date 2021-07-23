package org.bukkit.craftbukkit.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.Inventory;

public class CraftShulkerBox extends CraftLootable<ShulkerBoxBlockEntity> implements ShulkerBox {

    public CraftShulkerBox(final Block block) {
        super(block, ShulkerBoxBlockEntity.class);
    }

    public CraftShulkerBox(final Material material, final ShulkerBoxBlockEntity te) {
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

    @Override
    public DyeColor getColor() {
        net.minecraft.world.item.DyeColor color = ((ShulkerBoxBlock) CraftMagicNumbers.getBlock(this.getType())).color;

        return (color == null) ? null : DyeColor.getByWoolData((byte) color.getColorValue());
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().opened) {
            Level world = getTileEntity().getLevel();
            world.blockEvent(getPosition(), getTileEntity().getBlockState().getBlock(), 1, 1);
            world.playSound(null, getPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().opened) {
            Level world = getTileEntity().getLevel();
            world.blockEvent(getPosition(), getTileEntity().getBlockState().getBlock(), 1, 0);
            world.playSound(null, getPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = false;
    }
}
