package org.bukkit.craftbukkit.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.CommandBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCommandBlock extends CraftBlockData implements CommandBlock {

    private static final BooleanProperty CONDITIONAL = getBoolean("conditional");

    @Override
    public boolean isConditional() {
        return get(CONDITIONAL);
    }

    @Override
    public void setConditional(boolean conditional) {
        set(CONDITIONAL, conditional);
    }
}
