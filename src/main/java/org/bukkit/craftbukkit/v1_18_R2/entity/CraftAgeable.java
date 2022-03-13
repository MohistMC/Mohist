package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.AgeableMob;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Ageable;

public class CraftAgeable extends CraftCreature implements Ageable {
    public CraftAgeable(CraftServer server, AgeableMob entity) {
        super(server, entity);
    }

    @Override
    public int getAge() {
        return getHandle().getAge();
    }

    @Override
    public void setAge(int age) {
        getHandle().setAge(age);
    }

    @Override
    public void setAgeLock(boolean lock) {
        getHandle().ageLocked = lock;
    }

    @Override
    public boolean getAgeLock() {
        return getHandle().ageLocked;
    }

    @Override
    public void setBaby() {
        if (isAdult()) {
            setAge(-24000);
        }
    }

    @Override
    public void setAdult() {
        if (!isAdult()) {
            setAge(0);
        }
    }

    @Override
    public boolean isAdult() {
        return getAge() >= 0;
    }


    @Override
    public boolean canBreed() {
        return getAge() == 0;
    }

    @Override
    public void setBreed(boolean breed) {
        if (breed) {
            setAge(0);
        } else if (isAdult()) {
            setAge(6000);
        }
    }

    @Override
    public AgeableMob getHandle() {
        return (AgeableMob) entity;
    }

    @Override
    public String toString() {
        return "CraftAgeable";
    }
}
