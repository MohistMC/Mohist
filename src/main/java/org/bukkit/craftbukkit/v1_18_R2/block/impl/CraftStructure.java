/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

public final class CraftStructure extends org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData implements org.bukkit.block.data.type.StructureBlock {

    public CraftStructure() {
        super();
    }

    public CraftStructure(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_18_R2.block.data.type.CraftStructureBlock

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> MODE = getEnum(net.minecraft.world.level.block.StructureBlock.class, "mode");

    @Override
    public org.bukkit.block.data.type.StructureBlock.Mode getMode() {
        return get(MODE, org.bukkit.block.data.type.StructureBlock.Mode.class);
    }

    @Override
    public void setMode(org.bukkit.block.data.type.StructureBlock.Mode mode) {
        set(MODE, mode);
    }
}
