package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftDaylightDetector extends CraftBlockData implements DaylightDetector {

    private static final BooleanProperty INVERTED = getBoolean("inverted");

    @Override
    public boolean isInverted() {
        return get(INVERTED);
    }

    @Override
    public void setInverted(boolean inverted) {
        set(INVERTED, inverted);
    }
}
