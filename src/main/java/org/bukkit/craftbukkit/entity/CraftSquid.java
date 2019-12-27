package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.SquidEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;

public class CraftSquid extends CraftWaterMob implements Squid {

    public CraftSquid(CraftServer server, SquidEntity entity) {
        super(server, entity);
    }

    @Override
    public SquidEntity getHandle() {
        return (SquidEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSquid";
    }

    @Override
    public EntityType getType() {
        return EntityType.SQUID;
    }
}
