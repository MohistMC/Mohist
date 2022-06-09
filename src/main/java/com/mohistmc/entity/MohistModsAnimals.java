package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.animal.Animal;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftAnimals;
import org.bukkit.entity.EntityType;

/**
 * Mohist
 *
 * @author Malcolm - m1lc0lm
 * @Created at 20.02.2022 - 20:46 GMT+1
 * © Copyright 2021 / 2022 - M1lcolm
 */
public class MohistModsAnimals extends CraftAnimals
{
    public MohistModsAnimals ( CraftServer server, Animal entity )
    {
        super( server, entity );
        this.entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }


    @Override
    public Animal getHandle() {
        return (Animal) entity;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_ANIMAL;
        }
    }

    @Override
    public String toString() {
        return "CraftCustomTameableAnimal{" + entityName + '}';
    }
}
