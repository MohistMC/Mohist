package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import org.bukkit.World;
import org.bukkit.block.BlastFurnace;

public class CraftBlastFurnace extends CraftFurnace<BlastFurnaceBlockEntity> implements BlastFurnace {

    public CraftBlastFurnace(World world, BlastFurnaceBlockEntity te) {
        super(world, te);
    }

    protected CraftBlastFurnace(CraftBlastFurnace state) {
        super(state);
    }

    @Override
    public CraftBlastFurnace copy() {
        return new CraftBlastFurnace(this);
    }
}
