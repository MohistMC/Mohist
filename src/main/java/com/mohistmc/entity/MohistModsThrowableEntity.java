package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftProjectile;
import org.bukkit.entity.EntityType;

public class MohistModsThrowableEntity extends CraftProjectile {

    public String entityName;

    public MohistModsThrowableEntity(CraftServer server, ThrowableProjectile entity) {
        super(server, entity);
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public ThrowableProjectile getHandle() {
        return (ThrowableProjectile) entity;
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
        return "CraftCustomThrowableEntity{" + entityName + '}';
    }
}
