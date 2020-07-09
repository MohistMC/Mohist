package org.bukkit.craftbukkit.v1_15_R1.block.data;

import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.Directional;

public abstract class CraftDirectional extends CraftBlockData implements Directional {

    private static final EnumProperty<?> FACING = getEnum("facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }
}
