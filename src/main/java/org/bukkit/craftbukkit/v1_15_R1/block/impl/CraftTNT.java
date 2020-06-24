/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public final class CraftTNT extends CraftBlockData implements org.bukkit.block.data.type.TNT {

    public CraftTNT() {
        super();
    }

    public CraftTNT(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftTNT

    private static final BooleanProperty UNSTABLE = getBoolean(net.minecraft.block.TNTBlock.class, "unstable");

    @Override
    public boolean isUnstable() {
        return get(UNSTABLE);
    }

    @Override
    public void setUnstable(boolean unstable) {
        set(UNSTABLE, unstable);
    }
}
