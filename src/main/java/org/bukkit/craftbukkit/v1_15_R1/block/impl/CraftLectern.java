/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public final class CraftLectern extends org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.Lectern, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftLectern() {
        super();
    }

    public CraftLectern(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.type.CraftLectern

    private static final BooleanProperty HAS_BOOK = getBoolean(net.minecraft.block.LecternBlock.class, "has_book");

    @Override
    public boolean hasBook() {
        return get(HAS_BOOK);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftDirectional

    private static final EnumProperty<?> FACING = getEnum(net.minecraft.block.LecternBlock.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftPowerable

    private static final BooleanProperty POWERED = getBoolean(net.minecraft.block.LecternBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return get(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        set(POWERED, powered);
    }
}
