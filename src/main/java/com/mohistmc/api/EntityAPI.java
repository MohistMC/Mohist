package com.mohistmc.api;

import com.mohistmc.MohistConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntityType;

public class EntityAPI {

    public static String entityName(Entity entity) {
        String entityName = CraftEntityType.minecraftToBukkit(entity.getType()).name();
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
        return entityName;
    }


    public static boolean isBan(org.bukkit.entity.Entity entity) {
        return MohistConfig.ban_entity_types.contains(entity.getType().name());
    }

    public static String getNBTAsString(org.bukkit.entity.Entity entity) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        CompoundTag compoundTag = new CompoundTag();
        nmsEntity.save(compoundTag);
        return compoundTag.getAsString();
    }
}
