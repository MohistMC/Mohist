package org.bukkit.craftbukkit.v1_18_R2.block.data;

import org.bukkit.block.data.Lightable;

public abstract class CraftLightable extends CraftBlockData implements Lightable {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = getBoolean("lit");

    @Override
    public boolean isLit() {
        return get(LIT);
    }

    @Override
    public void setLit(boolean lit) {
        set(LIT, lit);
    }
}
