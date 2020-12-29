package com.mohistmc.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

public class EntityAPI {

    public static boolean isForgeVillager(Entity entity) {
        if (entity instanceof Villager) {
            Villager villager = (Villager) entity;
            Villager.Profession profession = villager.getProfession();
            return profession.ordinal() > 7;
        }
        return false;
    }
}
