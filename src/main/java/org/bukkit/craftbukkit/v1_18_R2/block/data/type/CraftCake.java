package org.bukkit.craftbukkit.v1_18_R2.block.data.type;

import org.bukkit.block.data.type.Cake;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public abstract class CraftCake extends CraftBlockData implements Cake {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty BITES = getInteger("bites");

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
