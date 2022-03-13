package org.bukkit.craftbukkit.v1_18_R2.block.data.type;

import org.bukkit.block.data.type.Door;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public abstract class CraftDoor extends CraftBlockData implements Door {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HINGE = getEnum("hinge");

    @Override
    public org.bukkit.block.data.type.Door.Hinge getHinge() {
        return get(HINGE, org.bukkit.block.data.type.Door.Hinge.class);
    }

    @Override
    public void setHinge(org.bukkit.block.data.type.Door.Hinge hinge) {
        set(HINGE, hinge);
    }
}
