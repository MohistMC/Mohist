package org.bukkit.craftbukkit.v1_18_R1.block;

import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Conduit;

public class CraftConduit extends CraftBlockEntityState<ConduitBlockEntity> implements Conduit {

    public CraftConduit(World world, ConduitBlockEntity te) {
        super(world, te);
    }
}
