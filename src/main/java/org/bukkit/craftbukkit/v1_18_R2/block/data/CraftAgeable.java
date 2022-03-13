package org.bukkit.craftbukkit.v1_18_R2.block.data;

import org.bukkit.block.data.Ageable;

public abstract class CraftAgeable extends CraftBlockData implements Ageable {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = getInteger("age");

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
