package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.TamableAnimal;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftTameableAnimal;
import org.bukkit.entity.EntityType;

/**
 * Mohist
 *
 * @author Malcolm - m1lc0lm
 * @Created at 20.02.2022 - 20:40 GMT+1
 * © Copyright 2021 / 2022 - M1lcolm
 */
public class MohistModsTameableEntity extends CraftTameableAnimal
{

    public String entityName;
    public MohistModsTameableEntity ( CraftServer server, TamableAnimal entity )
    {
        super( server, entity );
        this.entityName = EntityAPI.entityName(entity);
    }


    @Override
    public TamableAnimal getHandle() {
        return (TamableAnimal) entity;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_TAMEABLE_ANIMALS;
        }
    }

    @Override
    public String toString() {
        return "MohistModsTameableEntity{" + entityName + '}';
    }
}
