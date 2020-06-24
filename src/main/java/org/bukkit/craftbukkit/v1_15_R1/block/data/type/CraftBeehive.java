package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.IntegerProperty;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftBeehive extends CraftBlockData implements Beehive {

    private static final IntegerProperty HONEY_LEVEL = getInteger("honey_level");

    @Override
    public int getHoneyLevel() {
        return get(HONEY_LEVEL);
    }

    @Override
    public void setHoneyLevel(int honeyLevel) {
        set(HONEY_LEVEL, honeyLevel);
    }

    @Override
    public int getMaximumHoneyLevel() {
        return getMax(HONEY_LEVEL);
    }
}
