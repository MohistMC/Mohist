/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;

public final class CraftStairs extends org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.Stairs, org.bukkit.block.data.Bisected, org.bukkit.block.data.Directional, org.bukkit.block.data.Waterlogged {

    public CraftStairs() {
        super();
    }

    public CraftStairs(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.type.CraftStairs

    private static final EnumProperty<?> SHAPE = getEnum(net.minecraft.block.StairsBlock.class, "shape");

    @Override
    public Shape getShape() {
        return get(SHAPE, Shape.class);
    }

    @Override
    public void setShape(Shape shape) {
        set(SHAPE, shape);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBisected

    private static final EnumProperty<?> HALF = getEnum(net.minecraft.block.StairsBlock.class, "half");

    @Override
    public Half getHalf() {
        return get(HALF, Half.class);
    }

    @Override
    public void setHalf(Half half) {
        set(HALF, half);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftDirectional

    private static final EnumProperty<?> FACING = getEnum(net.minecraft.block.StairsBlock.class, "facing");

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

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftWaterlogged

    private static final BooleanProperty WATERLOGGED = getBoolean(net.minecraft.block.StairsBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
