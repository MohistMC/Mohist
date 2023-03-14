package org.bukkit.craftbukkit.v1_19_R3.block;

import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import org.bukkit.World;

public class CraftFurnaceFurnace extends CraftFurnace<FurnaceBlockEntity> {

    public CraftFurnaceFurnace(World world, FurnaceBlockEntity te) {
        super(world, te);
    }
}
