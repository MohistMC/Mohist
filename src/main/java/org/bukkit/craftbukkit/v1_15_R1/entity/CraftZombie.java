package org.bukkit.craftbukkit.v1_15_R1.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

public class CraftZombie extends CraftMonster implements Zombie {

    public CraftZombie(CraftServer server, ZombieEntity entity) {
        super(server, entity);
    }

    @Override
    public ZombieEntity getHandle() {
        return (ZombieEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftZombie";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE;
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
    public boolean isVillager() {
        return getHandle() instanceof ZombieVillagerEntity;
    }

    @Override
    public void setVillager(boolean flag) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        return null;
    }

    @Override
    public boolean isConverting() {
        return getHandle().isDrowning();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");

        return getHandle().drownedConversionTime;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().drownedConversionTime = -1;
            getHandle().getDataManager().set(ZombieEntity.DROWNING, false);
        } else {
            getHandle().startDrowning(time);
        }
    }
}
