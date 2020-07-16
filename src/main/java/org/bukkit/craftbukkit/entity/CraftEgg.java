package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.thrown.ThrownEggEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;

public class CraftEgg extends CraftThrowableProjectile implements Egg {
    public CraftEgg(CraftServer server, ThrownEggEntity entity) {
        super(server, entity);
    }

    @Override
    public ThrownEggEntity getHandle() {
        return (ThrownEggEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }

    @Override
    public EntityType getType() {
        return EntityType.EGG;
    }
}
