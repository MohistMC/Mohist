/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

public final class CraftBrewingStand extends org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData implements org.bukkit.block.data.type.BrewingStand {

    public CraftBrewingStand() {
        super();
    }

    public CraftBrewingStand(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.type.CraftBrewingStand

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty[] HAS_BOTTLE = new net.minecraft.world.level.block.state.properties.BooleanProperty[]{
        getBoolean(net.minecraft.world.level.block.BrewingStandBlock.class, "has_bottle_0"), getBoolean(net.minecraft.world.level.block.BrewingStandBlock.class, "has_bottle_1"), getBoolean(net.minecraft.world.level.block.BrewingStandBlock.class, "has_bottle_2")
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
