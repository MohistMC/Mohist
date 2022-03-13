package org.bukkit.craftbukkit.v1_18_R2.block.data.type;

import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public abstract class CraftStructureBlock extends CraftBlockData implements StructureBlock {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> MODE = getEnum("mode");

    @Override
    public org.bukkit.block.data.type.StructureBlock.Mode getMode() {
        return get(MODE, org.bukkit.block.data.type.StructureBlock.Mode.class);
    }

    @Override
    public void setMode(org.bukkit.block.data.type.StructureBlock.Mode mode) {
        set(MODE, mode);
    }
}
