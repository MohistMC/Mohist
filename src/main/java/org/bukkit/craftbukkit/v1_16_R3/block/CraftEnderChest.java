package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.EnderChestTileEntity;
import net.minecraft.util.SoundEvents;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<EnderChestTileEntity> implements EnderChest {

    public CraftEnderChest(final Block block) {
        super(block, EnderChestTileEntity.class);
    }

    public CraftEnderChest(final Material material, final EnderChestTileEntity te) {
        super(material, te);
    }

    // Paper start - More Lidded Block API
    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().opened) {
            net.minecraft.world.World world = getTileEntity().getLevel();
            world.blockEvent(getTileEntity().getBlockPos(), getTileEntity().getBlockState().getBlock(), 1, getTileEntity().getViewerCount() + 1);
            world.playSound(null, getPosition(), net.minecraft.util.SoundEvents.ENDER_CHEST_OPEN, net.minecraft.util.SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().opened) {
            net.minecraft.world.World world = getTileEntity().getLevel();
            world.blockEvent(getTileEntity().getBlockPos(), getTileEntity().getBlockState().getBlock(), 1, 0);
            world.playSound(null, getPosition(), SoundEvents.ENDER_CHEST_CLOSE, net.minecraft.util.SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = false;
    }

    @Override
    public boolean isOpen() {
        return getTileEntity().opened;
    }
    // Paper end - More Lidded Block API
}
