package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.FlyingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Flying;

public class CraftFlying extends CraftMob implements Flying {

    public CraftFlying(CraftServer server, FlyingEntity entity) {
        super(server, entity);
    }

    @Override
    public FlyingEntity getHandle() {
        return (FlyingEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFlying";
    }
}
