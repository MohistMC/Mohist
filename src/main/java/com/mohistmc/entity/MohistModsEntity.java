package com.mohistmc.entity;

import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;

public class MohistModsEntity extends CraftEntity {

    public String entityName;

    public MohistModsEntity(CraftServer server, net.minecraft.world.entity.Entity entity) {
        super(server, entity);
        entityName = entity.getName().getString();

    }

    @Override
    public net.minecraft.world.entity.Entity getHandle() {
        return this.entity;
    }

    @Override
    public String toString() {
        return "MohistModsEntity{" + entityName + '}';
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
