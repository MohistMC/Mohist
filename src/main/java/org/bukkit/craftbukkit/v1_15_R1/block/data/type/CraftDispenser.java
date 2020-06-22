package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftDispenser extends CraftBlockData implements Dispenser {

    private static final BooleanProperty TRIGGERED = getBoolean("triggered");

    @Override
    public boolean isTriggered() {
        return get(TRIGGERED);
    }

    @Override
    public void setTriggered(boolean triggered) {
        set(TRIGGERED, triggered);
    }
}
