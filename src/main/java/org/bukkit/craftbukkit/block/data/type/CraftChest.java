package org.bukkit.craftbukkit.block.data.type;

import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.type.Chest;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftChest extends CraftBlockData implements Chest {

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
