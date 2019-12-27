/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftLantern extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Lantern {

    public CraftLantern() {
        super();
    }

    public CraftLantern(net.minecraft.block.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftLantern

    private static final net.minecraft.state.PropertyBoolean HANGING = getBoolean(net.minecraft.block.BlockLantern.class, "hanging");

    @Override
    public boolean isHanging() {
        return get(HANGING);
    }

    @Override
    public void setHanging(boolean hanging) {
        set(HANGING, hanging);
    }
}
