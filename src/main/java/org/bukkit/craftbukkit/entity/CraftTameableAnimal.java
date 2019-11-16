package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;

import java.util.UUID;

public class CraftTameableAnimal extends CraftAnimals implements Tameable, Creature {
    public CraftTameableAnimal(CraftServer server, net.minecraft.entity.passive.EntityTameable entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.entity.passive.EntityTameable getHandle() {
        return (net.minecraft.entity.passive.EntityTameable)super.getHandle();
    }

    public UUID getOwnerUUID() {
        try {
            return UUID.fromString(getHandle().func_152113_b());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        if (uuid == null) {
            getHandle().func_152115_b("");
        } else {
            getHandle().func_152115_b(uuid.toString());
        }
    }

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

    public boolean isTamed() {
        return getHandle().isTamed();
    }

    public void setOwner(AnimalTamer tamer) {
        if (tamer != null) {
            setTamed(true);
            getHandle().setPathToEntity(null);
            setOwnerUUID(tamer.getUniqueId());
        } else {
            setTamed(false);
            setOwnerUUID(null);
    }
    }

    public void setTamed(boolean tame) {
        getHandle().setTamed(tame);
        if (!tame) {
            setOwnerUUID(null);
        }
    }

    public boolean isSitting() {
        return getHandle().isSitting();
    }

    public void setSitting(boolean sitting) {
        getHandle().func_70907_r().setSitting(sitting);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{owner=" + getOwner() + ",tamed=" + isTamed() + "}";
    }
}
