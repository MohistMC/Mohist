package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftThrowableProjectile;
import org.bukkit.entity.EntityType;

public class CraftCustomThrowableProjectile extends CraftThrowableProjectile {

    public String entityName;

    public CraftCustomThrowableProjectile(CraftServer server, ProjectileItemEntity entity) {
        super(server, entity);
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return "CraftCustomThrowableProjectile{" + entityName + '}';
    }
}
