package org.bukkit.craftbukkit.v1_18_R2.block.data.type;

import org.bukkit.block.data.type.Leaves;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public class CraftLeaves extends CraftBlockData implements Leaves {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DISTANCE = getInteger("distance");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty PERSISTENT = getBoolean("persistent");

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
