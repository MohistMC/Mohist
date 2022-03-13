package org.bukkit.craftbukkit.v1_18_R2.block.data.type;

import org.bukkit.block.data.type.Comparator;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public abstract class CraftComparator extends CraftBlockData implements Comparator {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> MODE = getEnum("mode");

    @Override
    public org.bukkit.block.data.type.Comparator.Mode getMode() {
        return get(MODE, org.bukkit.block.data.type.Comparator.Mode.class);
    }

    @Override
    public void setMode(org.bukkit.block.data.type.Comparator.Mode mode) {
        set(MODE, mode);
    }
}
