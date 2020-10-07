package com.mohistmc.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.EntityType;

public class CraftCustomEntity extends CraftEntity {

    public String entityName;

    public CraftCustomEntity(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
        this.entityName = ""; /*EntityRegistry.getCustomEntityTypeName(entity.getClass());*/
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public net.minecraft.entity.Entity getHandle() {
        return this.entity;
    }

    @Override
    public String toString() {
        return this.entityName;
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
    public String getCustomName() {
        String name = this.getHandle().getCustomName().getString();
        if (name == null || name.length() == 0) {
            return this.entity.getName().getString();
        }
        return name;
    }
}