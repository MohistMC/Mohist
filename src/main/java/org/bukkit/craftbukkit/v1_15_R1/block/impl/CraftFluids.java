/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.IntegerProperty;

public final class CraftFluids extends org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData implements org.bukkit.block.data.Levelled {

    public CraftFluids() {
        super();
    }

    public CraftFluids(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftLevelled

    private static final IntegerProperty LEVEL = getInteger(net.minecraft.block.FlowingFluidBlock.class, "level");

    @Override
    public int getLevel() {
        return get(LEVEL);
    }

    @Override
    public void setLevel(int level) {
        set(LEVEL, level);
    }

    @Override
    public int getMaximumLevel() {
        return getMax(LEVEL);
    }
}
