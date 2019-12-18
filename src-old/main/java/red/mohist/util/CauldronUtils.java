package red.mohist.util;

import net.minecraft.tileentity.TileEntity;
import org.bukkit.inventory.InventoryHolder;

public class CauldronUtils {

    public static InventoryHolder getOwner(TileEntity tileentity) {
        org.bukkit.block.BlockState state = tileentity.getWorld().getWorld().getBlockAt(tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ()).getState();

        if (state instanceof InventoryHolder) {
            return (InventoryHolder) state;
        }

        return null;
    }
}
