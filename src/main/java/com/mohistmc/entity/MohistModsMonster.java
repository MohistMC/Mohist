package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftMonster;
import org.bukkit.entity.EntityType;

/**
 * Mohist
 *
 * @author Malcolm - m1lc0lm
 * @Created at 20.02.2022 - 21:02 GMT+1
 * © Copyright 2021 / 2022 - M1lcolm
 */
public class MohistModsMonster extends CraftMonster
{
    public MohistModsMonster ( CraftServer server, Monster entity )
    {
        super( server, entity );
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }


    @Override
    public Monster getHandle() {
        return (Monster) entity;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_MONSTER;
        }
    }

    @Override
    public String toString() {
        return "CraftCustomMonster{" + entityName + '}';
    }
}
