package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.Piston;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftPiston extends CraftBlockData implements Piston {

    private static final BooleanProperty EXTENDED = getBoolean("extended");

    @Override
    public boolean isExtended() {
        return get(EXTENDED);
    }

    @Override
    public void setExtended(boolean extended) {
        set(EXTENDED, extended);
    }
}
