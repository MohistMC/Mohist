package org.bukkit.craftbukkit.v1_19_R3.block;

import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import org.bukkit.World;

public class CraftMovingPiston extends CraftBlockEntityState<PistonMovingBlockEntity> {

    public CraftMovingPiston(World world, PistonMovingBlockEntity tileEntity) {
        super(world, tileEntity);
    }
}
