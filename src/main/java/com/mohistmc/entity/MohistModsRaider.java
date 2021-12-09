package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.raid.Raider;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftRaider;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;

public class MohistModsRaider extends CraftRaider {

    public String entityName;

    public MohistModsRaider(CraftServer server, Raider entity) {
        super(server, entity);
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public Raider getHandle() {
        return (Raider) this.entity;
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
