package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Smoker;

public class CraftSmoker extends CraftFurnace<SmokerBlockEntity> implements Smoker {

    public CraftSmoker(World world, SmokerBlockEntity te) {
        super(world, te);
    }

    protected CraftSmoker(CraftSmoker state) {
        super(state);
    }

    @Override
    public CraftSmoker copy() {
        return new CraftSmoker(this);
    }
}
