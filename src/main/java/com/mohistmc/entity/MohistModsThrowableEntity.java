package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftProjectile;
import org.bukkit.entity.EntityType;

import java.util.Objects;

public class MohistModsThrowableEntity extends CraftProjectile {

    public String entityName;

    public MohistModsThrowableEntity(CraftServer server, ThrowableProjectile entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    @Override
    public ThrowableProjectile getHandle() {
        return (ThrowableProjectile) entity;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        return Objects.requireNonNullElse(type, EntityType.UNKNOWN);
    }

    @Override
    public String toString() {
        return "MohistModsThrowableEntity{" + entityName + '}';
    }
}
