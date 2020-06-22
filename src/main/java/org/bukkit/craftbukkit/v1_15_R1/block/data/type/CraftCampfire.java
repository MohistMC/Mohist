package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftCampfire extends CraftBlockData implements Campfire {

    private static final BooleanProperty SIGNAL_FIRE = getBoolean("signal_fire");

    @Override
    public boolean isSignalFire() {
        return get(SIGNAL_FIRE);
    }

    @Override
    public void setSignalFire(boolean signalFire) {
        set(SIGNAL_FIRE, signalFire);
    }
}
