package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.BellTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CraftBell extends CraftBlockEntityState<BellTileEntity> {

    public CraftBell(Block block) {
        super(block, BellTileEntity.class);
    }

    public CraftBell(Material material, BellTileEntity te) {
        super(material, te);
    }
}
