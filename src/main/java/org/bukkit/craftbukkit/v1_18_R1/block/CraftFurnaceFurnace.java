package org.bukkit.craftbukkit.v1_18_R1.block;

import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CraftFurnaceFurnace extends CraftFurnace<FurnaceBlockEntity> {

    public CraftFurnaceFurnace(World world, FurnaceBlockEntity te) {
        super(world, te);
    }
}
