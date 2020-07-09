/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;

public final class CraftBrewingStand extends org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.BrewingStand {

    public CraftBrewingStand() {
        super();
    }

    public CraftBrewingStand(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.type.CraftBrewingStand

    private static final BooleanProperty[] HAS_BOTTLE = new BooleanProperty[]{
        getBoolean(net.minecraft.block.BrewingStandBlock.class, "has_bottle_0"), getBoolean(net.minecraft.block.BrewingStandBlock.class, "has_bottle_1"), getBoolean(net.minecraft.block.BrewingStandBlock.class, "has_bottle_2")
    };

    @Override
    public boolean hasBottle(int bottle) {
        return get(HAS_BOTTLE[bottle]);
    }

    @Override
    public void setBottle(int bottle, boolean has) {
        set(HAS_BOTTLE[bottle], has);
    }

    @Override
    public java.util.Set<Integer> getBottles() {
        com.google.common.collect.ImmutableSet.Builder<Integer> bottles = com.google.common.collect.ImmutableSet.builder();

        for (int index = 0; index < getMaximumBottles(); index++) {
            if (hasBottle(index)) {
                bottles.add(index);
            }
        }

        return bottles.build();
    }

    @Override
    public int getMaximumBottles() {
        return HAS_BOTTLE.length;
    }
}
