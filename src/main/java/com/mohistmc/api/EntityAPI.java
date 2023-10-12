package com.mohistmc.api;

import com.mohistmc.MohistConfig;
import java.util.Objects;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityAPI {

    public static String entityName(Entity entity) {
        String entityName = ServerAPI.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
        return entityName;
    }

    public static EntityType entityType(String entityName) {
        EntityType type = EntityType.fromName(entityName);
        return Objects.requireNonNullElse(type, EntityType.UNKNOWN);
    }

    public static EntityType entityType(String entityName, EntityType defType) {
        EntityType type = EntityType.fromName(entityName);
        if (type != null) {
            return type;
        } else {
            return defType;
        }
    }

    public static boolean isBan(org.bukkit.entity.Entity entity) {
        return MohistConfig.ban_entity_types.contains(entity.getType().name());
    }
}
