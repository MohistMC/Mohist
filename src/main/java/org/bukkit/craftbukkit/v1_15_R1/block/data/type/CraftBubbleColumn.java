package org.bukkit.craftbukkit.v1_15_R1.block.data.type;

import net.minecraft.state.BooleanProperty;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public abstract class CraftBubbleColumn extends CraftBlockData implements BubbleColumn {

    private static final BooleanProperty DRAG = getBoolean("drag");

    @Override
    public boolean isDrag() {
        return get(DRAG);
    }

    @Override
    public void setDrag(boolean drag) {
        set(DRAG, drag);
    }
}
