package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.SnowballEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;

public class CraftSnowball extends CraftThrowableProjectile implements Snowball {
    public CraftSnowball(CraftServer server, SnowballEntity entity) {
        super(server, entity);
    }

    @Override
    public SnowballEntity getHandle() {
        return (SnowballEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSnowball";
    }

    @Override
    public EntityType getType() {
        return EntityType.SNOWBALL;
    }
}
