package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.DyeColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
    public CraftWolf(CraftServer server, WolfEntity wolf) {
        super(server, wolf);
    }

    @Override
    public boolean isAngry() {
        return getHandle().isAngry();
    }

    @Override
    public void setAngry(boolean angry) {
        getHandle().setAngry(angry);
    }

    @Override
    public WolfEntity getHandle() {
        return (WolfEntity) entity;
    }

    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().getCollarColor().getColorIndex());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        getHandle().setCollarColor(DyeColor.fromColorIndex(color.getWoolData()));
    }
}
