package org.bukkit.craftbukkit.block.data;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.Openable;

public abstract class CraftOpenable extends CraftBlockData implements Openable {

    private static final BooleanProperty OPEN = getBoolean("open");

    @Override
    public boolean isOpen() {
        return get(OPEN);
    }

    @Override
    public void setOpen(boolean open) {
        set(OPEN, open);
    }
}
