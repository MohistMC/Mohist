package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftTripwire extends CraftBlockData implements Tripwire {

    private static final BooleanProperty DISARMED = getBoolean("disarmed");

    @Override
    public boolean isDisarmed() {
        return get(DISARMED);
    }

    @Override
    public void setDisarmed(boolean disarmed) {
        set(DISARMED, disarmed);
    }
}
