package org.bukkit.craftbukkit.v1_15_R1.block.data;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.Waterlogged;

public abstract class CraftWaterlogged extends CraftBlockData implements Waterlogged {

    private static final BooleanProperty WATERLOGGED = getBoolean("waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
