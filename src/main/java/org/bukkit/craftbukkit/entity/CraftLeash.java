package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.decoration.LeadKnotEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;

public class CraftLeash extends CraftHanging implements LeashHitch {
    public CraftLeash(CraftServer server, LeadKnotEntity entity) {
        super(server, entity);
    }

    @Override
    public LeadKnotEntity getHandle() {
        return (LeadKnotEntity) entity;
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
