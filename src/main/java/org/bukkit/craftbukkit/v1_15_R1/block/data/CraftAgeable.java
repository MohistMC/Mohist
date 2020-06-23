package org.bukkit.craftbukkit.v1_15_R1.block.data;

import net.minecraft.state.IntegerProperty;
import org.bukkit.block.data.Ageable;

public abstract class CraftAgeable extends CraftBlockData implements Ageable {

    private static final IntegerProperty AGE = getInteger("age");

    @Override
    public int getAge() {
        return get(AGE);
    }

    @Override
    public void setAge(int age) {
        set(AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(AGE);
    }
}
