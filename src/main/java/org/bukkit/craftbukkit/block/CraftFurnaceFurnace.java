package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.FurnaceBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CraftFurnaceFurnace extends CraftFurnace {

    public CraftFurnaceFurnace(Block block) {
        super(block, FurnaceBlockEntity.class);
    }

    public CraftFurnaceFurnace(Material material, FurnaceBlockEntity te) {
        super(material, te);
    }
}
