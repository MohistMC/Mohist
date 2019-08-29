package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.EntityLeashKnot;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;

public class CraftLeash extends CraftHanging implements LeashHitch {
    public CraftLeash(CraftServer server, EntityLeashKnot entity) {
        super(server, entity);
    }

    @Override
    public EntityLeashKnot getHandle() {
        return (EntityLeashKnot) entity;
    }

    @Override
    public String toString() {
        return "CraftLeash";
    }

    public EntityType getType() {
        return EntityType.LEASH_HITCH;
    }
}
