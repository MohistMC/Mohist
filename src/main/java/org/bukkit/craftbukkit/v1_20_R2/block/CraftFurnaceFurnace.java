package org.bukkit.craftbukkit.v1_20_R2.block;

import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import org.bukkit.World;

public class CraftFurnaceFurnace extends CraftFurnace<FurnaceBlockEntity> {

    public CraftFurnaceFurnace(World world, FurnaceBlockEntity te) {
        super(world, te);
    }

    protected CraftFurnaceFurnace(CraftFurnaceFurnace state) {
        super(state);
    }

    @Override
    public CraftFurnaceFurnace copy() {
        return new CraftFurnaceFurnace(this);
    }
}
