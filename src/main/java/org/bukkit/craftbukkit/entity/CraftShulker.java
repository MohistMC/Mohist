package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.ShulkerEntity;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Shulker;

public class CraftShulker extends CraftGolem implements Shulker {

    public CraftShulker(CraftServer server, ShulkerEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftShulker";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHULKER;
    }

    @Override
    public ShulkerEntity getHandle() {
        return (ShulkerEntity) entity;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData(getHandle().getDataTracker().get(ShulkerEntity.COLOR));
    }

    @Override
    public void setColor(DyeColor color) {
        getHandle().getDataTracker().set(ShulkerEntity.COLOR, (color == null) ? 16 : color.getWoolData());
    }
}
