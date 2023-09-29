package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Conduit;

public class CraftConduit extends CraftBlockEntityState<ConduitBlockEntity> implements Conduit {

    public CraftConduit(World world, ConduitBlockEntity te) {
        super(world, te);
    }

    protected CraftConduit(CraftConduit state) {
        super(state);
    }

    @Override
    public CraftConduit copy() {
        return new CraftConduit(this);
    }
}
