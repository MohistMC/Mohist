package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.monster.ZombiePigmanEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {

    public CraftPigZombie(CraftServer server, ZombiePigmanEntity entity) {
        super(server, entity);
    }

    @Override
    public int getAnger() {
        return getHandle().angerLevel;
    }

    @Override
    public void setAnger(int level) {
        getHandle().angerLevel = level;
    }

    @Override
    public void setAngry(boolean angry) {
        setAnger(angry ? 400 : 0);
    }

    @Override
    public boolean isAngry() {
        return getAnger() > 0;
    }

    @Override
    public ZombiePigmanEntity getHandle() {
        return (ZombiePigmanEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftPigZombie";
    }

    @Override
    public EntityType getType() {
        return EntityType.PIG_ZOMBIE;
    }

    @Override
    public boolean isConverting() {
        return false;
    }

    @Override
    public int getConversionTime() {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }

    @Override
    public void setConversionTime(int time) {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }
}
