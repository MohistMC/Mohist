/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

public final class CraftBell extends org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData implements org.bukkit.block.data.type.Bell, org.bukkit.block.data.Directional, org.bukkit.block.data.Powerable {

    public CraftBell() {
        super();
    }

    public CraftBell(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.type.CraftBell

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ATTACHMENT = getEnum(net.minecraft.world.level.block.BellBlock.class, "attachment");

    @Override
    public org.bukkit.block.data.type.Bell.Attachment getAttachment() {
        return get(ATTACHMENT, org.bukkit.block.data.type.Bell.Attachment.class);
    }

    @Override
    public void setAttachment(org.bukkit.block.data.type.Bell.Attachment leaves) {
        set(ATTACHMENT, leaves);
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum(net.minecraft.world.level.block.BellBlock.class, "facing");

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

    // org.bukkit.craftbukkit.v1_18_R2.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.BellBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return get(POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        set(POWERED, powered);
    }
}
