package org.bukkit.craftbukkit.block.data;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.Lightable;

public abstract class CraftLightable extends CraftBlockData implements Lightable {

    private static final BooleanProperty LIT = getBoolean("lit");

    @Override
    public boolean isLit() {
        return get(LIT);
    }

    @Override
    public void setLit(boolean lit) {
        set(LIT, lit);
    }
}
