/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

public final class CraftDispenser extends org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData implements org.bukkit.block.data.type.Dispenser, org.bukkit.block.data.Directional {

    public CraftDispenser() {
        super();
    }

    public CraftDispenser(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.type.CraftDispenser

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty TRIGGERED = getBoolean(net.minecraft.world.level.block.DispenserBlock.class, "triggered");

    @Override
    public boolean isTriggered() {
        return get(TRIGGERED);
    }

    @Override
    public void setTriggered(boolean triggered) {
        set(TRIGGERED, triggered);
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.DispenserBlock.class, "facing");

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
}
