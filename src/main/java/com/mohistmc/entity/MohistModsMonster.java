package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.monster.Monster;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftMonster;
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
    public String entityName;

    public MohistModsMonster ( CraftServer server, Monster entity )
    {
        super( server, entity );
        this.entityName = EntityAPI.entityName(entity);
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
        return "MohistModsMonster{" + entityName + '}';
    }
}
