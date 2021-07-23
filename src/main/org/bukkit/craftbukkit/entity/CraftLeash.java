package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;

public class CraftLeash extends CraftHanging implements LeashHitch {
    public CraftLeash(CraftServer server, net.minecraft.world.entity.decoration.LeashFenceKnotEntity entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.decoration.LeashFenceKnotEntity getHandle() {
        return (net.minecraft.world.entity.decoration.LeashFenceKnotEntity) entity;
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
