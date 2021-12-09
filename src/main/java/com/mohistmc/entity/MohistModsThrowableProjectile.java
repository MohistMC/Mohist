package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftThrowableProjectile;
import org.bukkit.entity.EntityType;

public class MohistModsThrowableProjectile extends CraftThrowableProjectile {

    public String entityName;

    public MohistModsThrowableProjectile(CraftServer server, ThrowableItemProjectile entity) {
        super(server, entity);
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public ThrowableItemProjectile getHandle() {
        return (ThrowableItemProjectile) this.entity;
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
