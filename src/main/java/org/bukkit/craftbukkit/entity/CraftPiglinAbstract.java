package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PiglinAbstract;

public class CraftPiglinAbstract extends CraftMonster implements PiglinAbstract {

    public CraftPiglinAbstract(CraftServer server, AbstractPiglinEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isImmuneToZombification() {
        return getHandle().func_242335_eK();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        getHandle().func_242340_t(flag);
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");
        return getHandle().field_242334_c;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().field_242334_c = -1;
            getHandle().func_242340_t(false);
        } else {
            getHandle().field_242334_c = time;
        }
    }

    @Override
    public boolean isConverting() {
        return getHandle().func_242336_eL();
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

    @Override
    public AbstractPiglinEntity getHandle() {
        return (AbstractPiglinEntity) super.getHandle();
    }
}
