package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.PoweredMinecart;

@SuppressWarnings("deprecation")
public class CraftMinecartFurnace extends CraftMinecart implements PoweredMinecart {
    public CraftMinecartFurnace(CraftServer server, FurnaceMinecartEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartFurnace";
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART_FURNACE;
    }
}
