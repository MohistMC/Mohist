package org.bukkit.craftbukkit.v1_18_R2.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hoglin;

public class CraftHoglin extends CraftAnimals implements Hoglin {

    public CraftHoglin(CraftServer server, net.minecraft.world.entity.monster.hoglin.Hoglin entity) {
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
    public boolean isAbleToBeHunted() {
        return getHandle().cannotBeHunted;
    }

    @Override
    public void setIsAbleToBeHunted(boolean flag) {
        getHandle().cannotBeHunted = flag;
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
    public net.minecraft.world.entity.monster.hoglin.Hoglin getHandle() {
        return (net.minecraft.world.entity.monster.hoglin.Hoglin) entity;
    }

    @Override
    public String toString() {
        return "CraftHoglin";
    }

    @Override
    public EntityType getType() {
        return EntityType.HOGLIN;
    }
}
