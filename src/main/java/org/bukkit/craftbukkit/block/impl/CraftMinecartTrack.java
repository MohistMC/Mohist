/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftMinecartTrack extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.Rail {

    public CraftMinecartTrack() {
        super();
    }

    public CraftMinecartTrack(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftRail

    private static final net.minecraft.state.property.EnumProperty<?> SHAPE = getEnum(net.minecraft.block.RailBlock.class, "shape");

    @Override
    public Shape getShape() {
        return get(SHAPE, Shape.class);
    }

    @Override
    public void setShape(Shape shape) {
        set(SHAPE, shape);
    }

    @Override
    public java.util.Set<Shape> getShapes() {
        return getValues(SHAPE, Shape.class);
    }
}
