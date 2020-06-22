package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftTechnicalPiston extends CraftBlockData implements TechnicalPiston {

    private static final EnumProperty<?> TYPE = getEnum("type");

    @Override
    public Type getType() {
        return get(TYPE, Type.class);
    }

    @Override
    public void setType(Type type) {
        set(TYPE, type);
    }
}
