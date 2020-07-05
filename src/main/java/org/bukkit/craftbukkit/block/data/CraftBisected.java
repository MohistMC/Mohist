package org.bukkit.craftbukkit.block.data;

import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.Bisected;

public class CraftBisected extends CraftBlockData implements Bisected {

    private static final EnumProperty<?> HALF = getEnum("half");

    @Override
    public Half getHalf() {
        return get(HALF, Half.class);
    }

    @Override
    public void setHalf(Half half) {
        set(HALF, half);
    }
}
