/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

public final class CraftRespawnAnchor extends org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData implements org.bukkit.block.data.type.RespawnAnchor {

    public CraftRespawnAnchor() {
        super();
    }

    public CraftRespawnAnchor(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.type.CraftRespawnAnchor

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty CHARGES = getInteger(net.minecraft.world.level.block.RespawnAnchorBlock.class, "charges");

    @Override
    public int getCharges() {
        return get(CHARGES);
    }

    @Override
    public void setCharges(int charges) {
        set(CHARGES, charges);
    }

    @Override
    public int getMaximumCharges() {
        return getMax(CHARGES);
    }
}
