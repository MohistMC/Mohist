package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftRaider;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;

public class CraftCustomRaider extends CraftRaider {

    public String entityName;

    public CraftCustomRaider(CraftServer server, AbstractRaiderEntity entity) {
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
    public String toString() {
        return "CraftCustomRaider{" + entityName + '}';
    }

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.ILLAGER;
    }
}
