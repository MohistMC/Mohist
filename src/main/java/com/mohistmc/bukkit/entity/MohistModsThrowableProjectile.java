package com.mohistmc.bukkit.entity;

import com.mohistmc.api.EntityAPI;
import java.util.Objects;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftThrowableProjectile;
import org.bukkit.entity.EntityType;

public class MohistModsThrowableProjectile extends CraftThrowableProjectile {

    public String entityName;

    public MohistModsThrowableProjectile(CraftServer server, ThrowableItemProjectile entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    @Override
    public ThrowableItemProjectile getHandle() {
        return (ThrowableItemProjectile) this.entity;
    }


    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        return Objects.requireNonNullElse(type, EntityType.UNKNOWN);
    }

    @Override
    public String toString() {
        return "MohistModsThrowableProjectile{" + entityName + '}';
    }
}
