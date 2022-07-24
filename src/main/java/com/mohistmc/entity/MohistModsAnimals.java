package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
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
public class MohistModsAnimals extends CraftAnimals {

    public String entityName;

    public MohistModsAnimals(CraftServer server, Animal entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }


    @Override
    public Animal getHandle() {
        return (Animal) entity;
    }

    @Override
    public EntityType getType() {
        return EntityAPI.entityType(entityName, EntityType.FORGE_MOD_ANIMAL);
    }

    @Override
    public String toString() {
        return "MohistModsAnimals{" + entityName + '}';
    }
}
