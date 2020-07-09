/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_15_R1.block.impl;

import net.minecraft.state.BooleanProperty;

public final class CraftHugeMushroom extends org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData implements org.bukkit.block.data.MultipleFacing {

    public CraftHugeMushroom() {
        super();
    }

    public CraftHugeMushroom(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_15_R1.block.data.CraftMultipleFacing

    private static final BooleanProperty[] FACES = new BooleanProperty[]{
        getBoolean(net.minecraft.block.HugeMushroomBlock.class, "north", true), getBoolean(net.minecraft.block.HugeMushroomBlock.class, "east", true), getBoolean(net.minecraft.block.HugeMushroomBlock.class, "south", true), getBoolean(net.minecraft.block.HugeMushroomBlock.class, "west", true), getBoolean(net.minecraft.block.HugeMushroomBlock.class, "up", true), getBoolean(net.minecraft.block.HugeMushroomBlock.class, "down", true)
    };

    @Override
    public boolean hasFace(org.bukkit.block.BlockFace face) {
        BooleanProperty state = FACES[face.ordinal()];
        if (state == null) {
            throw new IllegalArgumentException("Non-allowed face " + face + ". Check MultipleFacing.getAllowedFaces.");
        }
        return get(state);
    }

    @Override
    public void setFace(org.bukkit.block.BlockFace face, boolean has) {
        BooleanProperty state = FACES[face.ordinal()];
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
}
