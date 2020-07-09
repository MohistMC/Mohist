package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftComparator extends CraftBlockData implements Comparator {

    private static final EnumProperty<?> MODE = getEnum("mode");

    @Override
    public Mode getMode() {
        return get(MODE, Mode.class);
    }

    @Override
    public void setMode(Mode mode) {
        set(MODE, mode);
    }
}
