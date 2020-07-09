/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public final class CraftTrapdoor extends org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.TrapDoor, org.bukkit.block.data.Bisected, org.bukkit.block.data.Directional, org.bukkit.block.data.Openable, org.bukkit.block.data.Powerable, org.bukkit.block.data.Waterlogged {

    public CraftTrapdoor() {
        super();
    }

    public CraftTrapdoor(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBisected

    private static final EnumProperty<?> HALF = getEnum(net.minecraft.block.TrapDoorBlock.class, "half");

    @Override
    public Half getHalf() {
        return get(HALF, Half.class);
    }

    @Override
    public void setHalf(Half half) {
        set(HALF, half);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftDirectional

    private static final EnumProperty<?> FACING = getEnum(net.minecraft.block.TrapDoorBlock.class, "facing");

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

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftOpenable

    private static final BooleanProperty OPEN = getBoolean(net.minecraft.block.TrapDoorBlock.class, "open");

    @Override
    public boolean isOpen() {
        return get(OPEN);
    }

    @Override
    public void setOpen(boolean open) {
        set(OPEN, open);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftPowerable

    private static final BooleanProperty POWERED = getBoolean(net.minecraft.block.TrapDoorBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return get(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        set(POWERED, powered);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftWaterlogged

    private static final BooleanProperty WATERLOGGED = getBoolean(net.minecraft.block.TrapDoorBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
