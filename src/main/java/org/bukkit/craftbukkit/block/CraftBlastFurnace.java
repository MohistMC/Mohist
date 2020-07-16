package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Block;

public class CraftBlastFurnace extends CraftFurnace implements BlastFurnace {

    public CraftBlastFurnace(Block block) {
        super(block, BlastFurnaceBlockEntity.class);
    }

    public CraftBlastFurnace(Material material, BlastFurnaceBlockEntity te) {
        super(material, te);
    }
}
