package org.bukkit.craftbukkit.v1_18_R2.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.PiglinAbstract;

public class CraftPiglinAbstract extends CraftMonster implements PiglinAbstract {

    public CraftPiglinAbstract(CraftServer server, net.minecraft.world.entity.monster.piglin.AbstractPiglin entity) {
        super(server, entity);
    }

    @Override
    public boolean isImmuneToZombification() {
        return getHandle().isImmuneToZombification();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        getHandle().setImmuneToZombification(flag);
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");
        return getHandle().timeInOverworld;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().timeInOverworld = -1;
            getHandle().setImmuneToZombification(false);
        } else {
            getHandle().timeInOverworld = time;
        }
    }

    @Override
    public boolean isConverting() {
        return getHandle().isConverting();
    }

    @Override
    public boolean isBaby() {
        return getHandle().isBaby();
    }

    @Override
    public void setBaby(boolean flag) {
        getHandle().setBaby(flag);
    }

    @Override
    public int getAge() {
        return getHandle().isBaby() ? -1 : 0;
    }

    @Override
    public void setAge(int i) {
        getHandle().setBaby(i < 0);
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
        getHandle().setBaby(true);
    }

    @Override
    public void setAdult() {
        getHandle().setBaby(false);
    }

    @Override
    public boolean isAdult() {
        return !getHandle().isBaby();
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public void setBreed(boolean b) {
    }

    @Override
    public net.minecraft.world.entity.monster.piglin.AbstractPiglin getHandle() {
        return (net.minecraft.world.entity.monster.piglin.AbstractPiglin) super.getHandle();
    }
}
