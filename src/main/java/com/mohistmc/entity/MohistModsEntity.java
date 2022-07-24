package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;

public class MohistModsEntity extends CraftEntity {

    public String entityName;

    public MohistModsEntity(CraftServer server, net.minecraft.world.entity.Entity entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
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
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_CHEST_HORSE;
        }
    }
}
