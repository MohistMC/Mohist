package org.bukkit.craftbukkit.v1_16_R1.block.impl;

import org.bukkit.craftbukkit.v1_16_R1.block.data.CraftBlockData;

public final class CraftCobbleWall extends CraftBlockData implements org.bukkit.block.data.type.Wall, org.bukkit.block.data.Waterlogged {

    public CraftCobbleWall() {
        super();
    }

    public CraftCobbleWall(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftWall

    private static final net.minecraft.state.property.EnumProperty<?>[] HEIGHTS = new net.minecraft.state.property.EnumProperty[]{
        getEnum(net.minecraft.block.WallBlock.class, "north"), getEnum(net.minecraft.block.WallBlock.class, "east"), getEnum(net.minecraft.block.WallBlock.class, "south"), getEnum(net.minecraft.block.WallBlock.class, "west")
    };

    @Override
    public Height getHeight(org.bukkit.block.BlockFace face) {
        return get(HEIGHTS[face.ordinal()], Height.class);
    }

    @Override
    public void setHeight(org.bukkit.block.BlockFace face, Height height) {
        set(HEIGHTS[face.ordinal()], height);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.state.property.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.block.WallBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }

    @Override
    public boolean isUp() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setUp(boolean arg0) {
        // TODO Auto-generated method stub
    }

}