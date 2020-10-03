package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.ZoglinEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zoglin;

public class CraftZoglin extends CraftMonster implements Zoglin {

    public CraftZoglin(CraftServer server, ZoglinEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isBaby() {
        return getHandle().isChild();
    }

    @Override
    public void setBaby(boolean flag) {
        getHandle().setChild(flag);
    }

    @Override
    public ZoglinEntity getHandle() {
        return (ZoglinEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftZoglin";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOGLIN;
    }

    @Override
    public int getAge() {
        return getHandle().isChild() ? -1 : 0;
    }

    @Override
    public void setAge(int i) {
        getHandle().setChild(i < 0);
    }

    @Override
    public void setAgeLock(boolean b) {
    }

    @Override
    public boolean getAgeLock() {
        return false;
    }

    @Override
    public void setBaby() {
        getHandle().setChild(true);
    }

    @Override
    public void setAdult() {
        getHandle().setChild(false);
    }

    @Override
    public boolean isAdult() {
        return !getHandle().isChild();
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public void setBreed(boolean b) {
    }
}
