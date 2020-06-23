package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.IntegerProperty;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftFarmland extends CraftBlockData implements Farmland {

    private static final IntegerProperty MOISTURE = getInteger("moisture");

    @Override
    public int getMoisture() {
        return get(MOISTURE);
    }

    @Override
    public void setMoisture(int moisture) {
        set(MOISTURE, moisture);
    }

    @Override
    public int getMaximumMoisture() {
        return getMax(MOISTURE);
    }
}
