package org.bukkit.craftbukkit.v1_18_R2.entity;

import java.util.UUID;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;

public class CraftTameableAnimal extends CraftAnimals implements Tameable, Creature {
    public CraftTameableAnimal(CraftServer server, net.minecraft.world.entity.TamableAnimal entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.TamableAnimal getHandle() {
        return (net.minecraft.world.entity.TamableAnimal) super.getHandle();
    }

    public UUID getOwnerUUID() {
        try {
            return getHandle().getOwnerUUID();
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        getHandle().setOwnerUUID(uuid);
    }

    @Override
    public AnimalTamer getOwner() {
        if (getOwnerUUID() == null) {
            return null;
        }

        AnimalTamer owner = getServer().getPlayer(getOwnerUUID());
        if (owner == null) {
            owner = getServer().getOfflinePlayer(getOwnerUUID());
        }

        return owner;
    }

    @Override
    public boolean isTamed() {
        return getHandle().isTame();
    }

    @Override
    public void setOwner(AnimalTamer tamer) {
        if (tamer != null) {
            setTamed(true);
            getHandle().setTarget(null, null, false);
            setOwnerUUID(tamer.getUniqueId());
        } else {
            setTamed(false);
            setOwnerUUID(null);
        }
    }

    @Override
    public void setTamed(boolean tame) {
        getHandle().setTame(tame);
        if (!tame) {
            setOwnerUUID(null);
        }
    }

    public boolean isSitting() {
        return getHandle().isOrderedToSit();
    }

    public void setSitting(boolean sitting) {
        getHandle().setInSittingPose(sitting);
        getHandle().setOrderedToSit(sitting);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{owner=" + getOwner() + ",tamed=" + isTamed() + "}";
    }
}
