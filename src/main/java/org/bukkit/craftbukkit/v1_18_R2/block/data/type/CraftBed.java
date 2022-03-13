package org.bukkit.craftbukkit.v1_18_R2.block.data.type;

import org.bukkit.block.data.type.Bed;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public abstract class CraftBed extends CraftBlockData implements Bed {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> PART = getEnum("part");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty OCCUPIED = getBoolean("occupied");

    @Override
    public org.bukkit.block.data.type.Bed.Part getPart() {
        return get(PART, org.bukkit.block.data.type.Bed.Part.class);
    }

    @Override
    public void setPart(org.bukkit.block.data.type.Bed.Part part) {
        set(PART, part);
    }

    @Override
    public boolean isOccupied() {
        return get(OCCUPIED);
    }
}
