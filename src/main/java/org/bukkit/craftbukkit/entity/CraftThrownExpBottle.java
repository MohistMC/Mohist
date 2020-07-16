package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.thrown.ThrownExperienceBottleEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftThrowableProjectile implements ThrownExpBottle {
    public CraftThrownExpBottle(CraftServer server, ThrownExperienceBottleEntity entity) {
        super(server, entity);
    }

    @Override
    public ThrownExperienceBottleEntity getHandle() {
        return (ThrownExperienceBottleEntity) entity;
    }

    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }

    @Override
    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}
