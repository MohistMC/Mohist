package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.IntegerProperty;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftSeaPickle extends CraftBlockData implements SeaPickle {

    private static final IntegerProperty PICKLES = getInteger("pickles");

    @Override
    public int getPickles() {
        return get(PICKLES);
    }

    @Override
    public void setPickles(int pickles) {
        set(PICKLES, pickles);
    }

    @Override
    public int getMinimumPickles() {
        return getMin(PICKLES);
    }

    @Override
    public int getMaximumPickles() {
        return getMax(PICKLES);
    }
}
