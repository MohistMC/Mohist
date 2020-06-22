package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public class CraftLeaves extends CraftBlockData implements Leaves {

    private static final IntegerProperty DISTANCE = getInteger("distance");
    private static final BooleanProperty PERSISTENT = getBoolean("persistent");

    @Override
    public boolean isPersistent() {
        return get(PERSISTENT);
    }

    @Override
    public void setPersistent(boolean persistent) {
        set(PERSISTENT, persistent);
    }

    @Override
    public int getDistance() {
        return get(DISTANCE);
    }

    @Override
    public void setDistance(int distance) {
        set(DISTANCE, distance);
    }
}
