/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_20_R1.block.impl;

import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData;

public final class CraftSculkCatalyst extends CraftBlockData implements org.bukkit.block.data.type.SculkCatalyst {

    public CraftSculkCatalyst() {
        super();
    }

    public CraftSculkCatalyst(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSculkCatalyst

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty BLOOM = getBoolean(net.minecraft.world.level.block.SculkCatalystBlock.class, "bloom");

    @Override
    public boolean isBloom() {
        return get(BLOOM);
    }

    @Override
    public void setBloom(boolean bloom) {
        set(BLOOM, bloom);
    }
}
