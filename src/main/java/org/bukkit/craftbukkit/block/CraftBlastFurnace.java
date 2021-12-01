package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Block;

public class CraftBlastFurnace extends CraftFurnace<BlastFurnaceBlockEntity> implements BlastFurnace {

    public CraftBlastFurnace(World world, BlastFurnaceBlockEntity te) {
        super(world, te);
    }
}
