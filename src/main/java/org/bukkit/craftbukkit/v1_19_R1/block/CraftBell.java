package org.bukkit.craftbukkit.v1_19_R1.block;

import net.minecraft.world.level.block.entity.BellBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Bell;

public class CraftBell extends CraftBlockEntityState<BellBlockEntity> implements Bell {

    public CraftBell(World world, BellBlockEntity te) {
        super(world, te);
    }
}
