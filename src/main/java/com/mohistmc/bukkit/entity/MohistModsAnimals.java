package com.mohistmc.bukkit.entity;

import net.minecraft.world.entity.animal.Animal;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftAnimals;

/**
 * Mohist
 *
 * @author Malcolm - m1lc0lm
 * @Created at 20.02.2022 - 20:46 GMT+1
 * © Copyright 2021 / 2022 - M1lcolm
 */
public class MohistModsAnimals extends CraftAnimals {

    public MohistModsAnimals(CraftServer server, Animal entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsAnimals{" + getType() + '}';
    }
}
