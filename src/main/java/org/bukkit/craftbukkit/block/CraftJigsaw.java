package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.JigsawTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jigsaw;

public class CraftJigsaw extends CraftBlockEntityState<TileEntityJigsaw> implements Jigsaw {

    public CraftJigsaw(Block block) {
        super(block, TileEntityJigsaw.class);
    }

    public CraftJigsaw(Material material, TileEntityJigsaw te) {
        super(material, te);
    }
}
