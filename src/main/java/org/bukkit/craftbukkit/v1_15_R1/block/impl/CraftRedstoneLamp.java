/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;

public final class CraftRedstoneLamp extends org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData implements org.bukkit.block.data.Lightable {

    public CraftRedstoneLamp() {
        super();
    }

    public CraftRedstoneLamp(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftLightable

    private static final BooleanProperty LIT = getBoolean(net.minecraft.block.RedstoneLampBlock.class, "lit");

    @Override
    public boolean isLit() {
        return get(LIT);
    }

    @Override
    public void setLit(boolean lit) {
        set(LIT, lit);
    }
}
