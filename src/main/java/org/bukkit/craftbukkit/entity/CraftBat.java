package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.BatEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;

public class CraftBat extends CraftAmbient implements Bat {
    public CraftBat(CraftServer server, BatEntity entity) {
        super(server, entity);
    }

    @Override
    public BatEntity getHandle() {
        return (BatEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftBat";
    }

    @Override
    public EntityType getType() {
        return EntityType.BAT;
    }

    @Override
    public boolean isAwake() {
        return !getHandle().getIsBatHanging();
    }

    @Override
    public void setAwake(boolean state) {
        getHandle().setIsBatHanging(!state);
    }
}
