package org.bukkit.craftbukkit.v1_15_R1.block.data;

import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.Orientable;

public class CraftOrientable extends CraftBlockData implements Orientable {

    private static final EnumProperty<?> AXIS = getEnum("axis");

    @Override
    public org.bukkit.Axis getAxis() {
        return get(AXIS, org.bukkit.Axis.class);
    }

    @Override
    public void setAxis(org.bukkit.Axis axis) {
        set(AXIS, axis);
    }

    @Override
    public java.util.Set<org.bukkit.Axis> getAxes() {
        return getValues(AXIS, org.bukkit.Axis.class);
    }
}
