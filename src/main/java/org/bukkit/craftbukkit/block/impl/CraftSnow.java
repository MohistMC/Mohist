/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftSnow extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Snow {

    public CraftSnow() {
        super();
    }

    public CraftSnow(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftSnow

    private static final net.minecraft.state.property.IntProperty LAYERS = getInteger(net.minecraft.block.SnowBlock.class, "layers");

    @Override
    public int getLayers() {
        return get(LAYERS);
    }

    @Override
    public void setLayers(int layers) {
        set(LAYERS, layers);
    }

    @Override
    public int getMinimumLayers() {
        return getMin(LAYERS);
    }

    @Override
    public int getMaximumLayers() {
        return getMax(LAYERS);
    }
}
