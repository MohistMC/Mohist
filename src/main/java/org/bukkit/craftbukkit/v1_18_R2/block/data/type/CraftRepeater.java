package org.bukkit.craftbukkit.v1_18_R2.block.data.type;

import org.bukkit.block.data.type.Repeater;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public abstract class CraftRepeater extends CraftBlockData implements Repeater {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty DELAY = getInteger("delay");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LOCKED = getBoolean("locked");

    @Override
    public int getDelay() {
        return get(DELAY);
    }

    @Override
    public void setDelay(int delay) {
        set(DELAY, delay);
    }

    @Override
    public int getMinimumDelay() {
        return getMin(DELAY);
    }

    @Override
    public int getMaximumDelay() {
        return getMax(DELAY);
    }

    @Override
    public boolean isLocked() {
        return get(LOCKED);
    }

    @Override
    public void setLocked(boolean locked) {
        set(LOCKED, locked);
    }
}
