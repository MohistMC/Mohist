package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftProjectile;
import org.bukkit.entity.EntityType;

public class MohistModsProjectileEntity extends CraftProjectile {
    public String entityName;

    public MohistModsProjectileEntity(CraftServer server, Projectile entity) {
        super(server, entity);
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public Projectile getHandle() {
        return (Projectile) this.entity;
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

