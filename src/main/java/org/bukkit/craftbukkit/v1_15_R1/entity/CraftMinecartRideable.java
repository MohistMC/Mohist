package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.RideableMinecart;

public class CraftMinecartRideable extends CraftMinecart implements RideableMinecart {
    public CraftMinecartRideable(CraftServer server, AbstractMinecartEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartRideable";
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART;
    }
}
