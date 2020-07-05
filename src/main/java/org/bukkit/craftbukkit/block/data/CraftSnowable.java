package org.bukkit.craftbukkit.block.data;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.Snowable;

public abstract class CraftSnowable extends CraftBlockData implements Snowable {

    private static final BooleanProperty SNOWY = getBoolean("snowy");

    @Override
    public boolean isSnowy() {
        return get(SNOWY);
    }

    @Override
    public void setSnowy(boolean snowy) {
        set(SNOWY, snowy);
    }
}
