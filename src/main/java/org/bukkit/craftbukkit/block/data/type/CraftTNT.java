package org.bukkit.craftbukkit.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.TNT;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftTNT extends CraftBlockData implements TNT {

    private static final BooleanProperty UNSTABLE = getBoolean("unstable");

    @Override
    public boolean isUnstable() {
        return get(UNSTABLE);
    }

    @Override
    public void setUnstable(boolean unstable) {
        set(UNSTABLE, unstable);
    }
}
