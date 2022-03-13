package org.bukkit.craftbukkit.v1_18_R2.block.data.type;

import org.bukkit.block.data.type.Switch;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public abstract class CraftSwitch extends CraftBlockData implements Switch {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACE = getEnum("face");

    @Override
    public org.bukkit.block.data.type.Switch.Face getFace() {
        return get(FACE, org.bukkit.block.data.type.Switch.Face.class);
    }

    @Override
    public void setFace(org.bukkit.block.data.type.Switch.Face face) {
        set(FACE, face);
    }
}
