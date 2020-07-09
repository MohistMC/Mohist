package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.type.Door;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftDoor extends CraftBlockData implements Door {

    private static final EnumProperty<?> HINGE = getEnum("hinge");

    @Override
    public Hinge getHinge() {
        return get(HINGE, Hinge.class);
    }

    @Override
    public void setHinge(Hinge hinge) {
        set(HINGE, hinge);
    }
}
