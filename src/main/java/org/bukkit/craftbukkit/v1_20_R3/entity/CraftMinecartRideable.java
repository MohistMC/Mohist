package org.bukkit.craftbukkit.v1_20_R3.entity;

import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.entity.minecart.RideableMinecart;

public class CraftMinecartRideable extends CraftMinecart implements RideableMinecart {
    public CraftMinecartRideable(CraftServer server, net.minecraft.world.entity.vehicle.AbstractMinecart entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartRideable";
    }
}
