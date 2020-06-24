package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.IntegerProperty;
import org.bukkit.block.data.type.Cake;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftCake extends CraftBlockData implements Cake {

    private static final IntegerProperty BITES = getInteger("bites");

    @Override
    public int getBites() {
        return get(BITES);
    }

    @Override
    public void setBites(int bites) {
        set(BITES, bites);
    }

    @Override
    public int getMaximumBites() {
        return getMax(BITES);
    }
}
