package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;

public class MohistModsLivingEntity extends CraftLivingEntity {

    public String entityName;

    public MohistModsLivingEntity(CraftServer server, LivingEntity entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    @Override
    public EntityType getType() {
        return EntityType.fromName(this.entityName);
    }

    @Override
    public String toString() {
        return "MohistModsLivingEntity{" + entityName + '}';
    }
}
