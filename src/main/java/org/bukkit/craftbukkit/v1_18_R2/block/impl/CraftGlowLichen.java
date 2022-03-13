/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

public final class CraftGlowLichen extends org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData implements org.bukkit.block.data.type.GlowLichen, org.bukkit.block.data.MultipleFacing, org.bukkit.block.data.Waterlogged {

    public CraftGlowLichen() {
        super();
    }

    public CraftGlowLichen(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.CraftMultipleFacing

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty[] FACES = new net.minecraft.world.level.block.state.properties.BooleanProperty[]{
        getBoolean(net.minecraft.world.level.block.GlowLichenBlock.class, "north", true), getBoolean(net.minecraft.world.level.block.GlowLichenBlock.class, "east", true), getBoolean(net.minecraft.world.level.block.GlowLichenBlock.class, "south", true), getBoolean(net.minecraft.world.level.block.GlowLichenBlock.class, "west", true), getBoolean(net.minecraft.world.level.block.GlowLichenBlock.class, "up", true), getBoolean(net.minecraft.world.level.block.GlowLichenBlock.class, "down", true)
    };

    @Override
    public boolean hasFace(org.bukkit.block.BlockFace face) {
        net.minecraft.world.level.block.state.properties.BooleanProperty state = FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        return get(state);
    }

    @Override
    public void setFace(org.bukkit.block.BlockFace face, boolean has) {
        net.minecraft.world.level.block.state.properties.BooleanProperty state = FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        set(state, has);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < FACES.length; i++) {
            if (FACES[i] != null && get(FACES[i])) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getAllowedFaces() {
        com.google.common.collect.ImmutableSet.Builder<org.bukkit.block.BlockFace> faces = com.google.common.collect.ImmutableSet.builder();

        for (int i = 0; i < FACES.length; i++) {
            if (FACES[i] != null) {
                faces.add(org.bukkit.block.BlockFace.values()[i]);
            }
        }

        return faces.build();
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.GlowLichenBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
