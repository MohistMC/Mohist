package com.mohistmc.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

public class HopperHelper {

    public static TileEntityHopper getHopper(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityHopper) {
            return (TileEntityHopper) tileEntity;
        }
        return null;
    }

    public static IInventory getInventory(World world, int x, int y, int z) {
        Block block = world.getType(x, y, z);
        if (block instanceof BlockChest) {
            return ((BlockChest) block).func_149951_m(world, x, y, z);
        }
        if (block.hasTileEntity(0)) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof IInventory) return (IInventory) tile;
        }
        return null;
    }
}
