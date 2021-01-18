package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftProjectile;
import org.bukkit.entity.EntityType;

public class CraftCustomProjectileEntity extends CraftProjectile {
    public String entityName;

    public CraftCustomProjectileEntity(CraftServer server, ProjectileEntity entity) {
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
            return EntityType.FORGE_MOD_PROJECTILE;
        }
    }

    @Override
    public String toString()
    {
        return "CustomProjectileEntity{" + entityName + '}';
    }
}

