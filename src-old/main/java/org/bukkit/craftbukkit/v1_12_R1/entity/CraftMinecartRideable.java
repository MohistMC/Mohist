package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.item.EntityMinecartEmpty;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.RideableMinecart;

public class CraftMinecartRideable extends CraftMinecart implements RideableMinecart {
    public CraftMinecartRideable(CraftServer server, EntityMinecartEmpty entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartRideable";
    }

    public EntityType getType() {
        return EntityType.MINECART;
    }
}
