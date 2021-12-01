package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.BellBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;

public class CraftBell extends CraftBlockEntityState<BellBlockEntity> implements Bell {

    public CraftBell(World world, BellBlockEntity te) {
        super(world, te);
    }
}
