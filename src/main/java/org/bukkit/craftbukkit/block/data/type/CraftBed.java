package org.bukkit.craftbukkit.block.data.type;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import org.bukkit.block.data.type.Bed;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftBed extends CraftBlockData implements Bed {

    private static final EnumProperty<?> PART = getEnum("part");
    private static final BooleanProperty OCCUPIED = getBoolean("occupied");

    @Override
    public Part getPart() {
        return get(PART, Part.class);
    }

    @Override
    public void setPart(Part part) {
        set(PART, part);
    }

    @Override
    public boolean isOccupied() {
        return get(OCCUPIED);
    }
}
