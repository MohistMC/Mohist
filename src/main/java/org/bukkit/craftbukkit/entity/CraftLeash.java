package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.LeashKnotEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;

public class CraftLeash extends CraftHanging implements LeashHitch {
    public CraftLeash(CraftServer server, LeashKnotEntity entity) {
        super(server, entity);
    }

    @Override
    public LeashKnotEntity getHandle() {
        return (LeashKnotEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftLeash";
    }

    @Override
    public EntityType getType() {
        return EntityType.LEASH_HITCH;
    }
}
