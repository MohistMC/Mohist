package com.mohistmc.inventory;

import com.mohistmc.MohistMC;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Mgazul
 * @date 2020/4/10 13:39
 */
public class InventoryOwner {

    public static InventoryHolder get(TileEntity te) {
        return get(te.world, te.getPos());
    }

    public static InventoryHolder get(IInventory inventory) {
        try {
            return inventory.getOwner();
        } catch (AbstractMethodError e) {
            return (inventory instanceof TileEntity) ? get((TileEntity) inventory) : null;
        }
    }

    public static InventoryHolder get(World world, BlockPos pos) {
        if (world == null) return null;
        // Spigot start
        org.bukkit.block.Block block = world.getCBWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        if (block == null) {
            MohistMC.LOGGER.warn("No block for owner at %s %d %d %d", new Object[]{world.getCBWorld(), pos.getX(), pos.getY(), pos.getZ()});
            return null;
        }
        // Spigot end
        org.bukkit.block.BlockState state = block.getState();
        if (state instanceof InventoryHolder) {
            return (InventoryHolder) state;
        } else if (state instanceof CraftBlockEntityState) {
            TileEntity te = ((CraftBlockEntityState) state).getTileEntity();
            if (te instanceof IInventory) {
                return new CraftCustomInventory((IInventory) te);
            }
        }
        return null;
    }
}
