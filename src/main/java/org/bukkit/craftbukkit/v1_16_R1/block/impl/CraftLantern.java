/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_16_R1.block.impl;

import org.bukkit.craftbukkit.v1_16_R1.block.data.CraftBlockData;

public final class CraftLantern extends CraftBlockData implements org.bukkit.block.data.type.Lantern {

    public CraftLantern() {
        super();
    }

    public CraftLantern(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLantern

    private static final net.minecraft.state.property.BooleanProperty HANGING = getBoolean(net.minecraft.block.LanternBlock.class, "hanging");

    @Override
    public boolean isHanging() {
        return get(HANGING);
    }

    @Override
    public void setHanging(boolean hanging) {
        set(HANGING, hanging);
    }
}
