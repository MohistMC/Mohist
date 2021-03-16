package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.entity.projectile.ThrowableEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftProjectile;
import org.bukkit.entity.EntityType;
public class CraftCustomThrowableEntity extends CraftProjectile {

    public String entityName;

    public CraftCustomThrowableEntity(CraftServer server, ThrowableEntity entity) {
        super(server, entity);
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    // Mohist start - Fix Savage&Ravage cast exception
    @Override
    public ThrowableEntity getHandle() {
        return (ThrowableEntity) entity;
    }
    // Mohist end

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
        return "CraftCustomThrowableEntity{" + entityName + '}';
    }
}
