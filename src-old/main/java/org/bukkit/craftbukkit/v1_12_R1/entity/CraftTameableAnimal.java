package org.bukkit.craftbukkit.v1_12_R1.entity;

import java.util.UUID;
import net.minecraft.entity.passive.EntityTameable;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;

public class CraftTameableAnimal extends CraftAnimals implements Tameable, Creature {
    public CraftTameableAnimal(CraftServer server, EntityTameable entity) {
        super(server, entity);
    }

    @Override
    public EntityTameable getHandle() {
        return (EntityTameable) super.getHandle();
    }

    public UUID getOwnerUUID() {
        try {
            return getHandle().getOwnerId();
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        getHandle().setOwnerId(uuid);
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

    public void setOwner(AnimalTamer tamer) {
        if (tamer != null) {
            setTamed(true);
            getHandle().setGoalTarget(null, null, false);
            setOwnerUUID(tamer.getUniqueId());
        } else {
            setTamed(false);
            setOwnerUUID(null);
        }
    }

    public boolean isTamed() {
        return getHandle().isTamed();
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
        getHandle().setSitting(sitting);
        getHandle().getAISit().setSitting(sitting);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{owner=" + getOwner() + ",tamed=" + isTamed() + "}";
    }
}
