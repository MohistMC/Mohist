package com.mohistmc.api;

import net.minecraft.world.entity.Entity;

public class EntityAPI {

    public static String entityName(Entity entity) {
        return entity.getType().getDescriptionId();
    }
}
