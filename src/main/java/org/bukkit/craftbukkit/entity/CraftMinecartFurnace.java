package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.minecart.PoweredMinecart;

@SuppressWarnings("deprecation")
public class CraftMinecartFurnace extends CraftMinecart implements PoweredMinecart {
    public CraftMinecartFurnace(CraftServer server, net.minecraft.world.entity.vehicle.MinecartFurnace entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.vehicle.MinecartFurnace getHandle() {
        return (net.minecraft.world.entity.vehicle.MinecartFurnace) entity;
    }

    @Override
    public int getFuel() {
        return getHandle().fuel;
    }

    @Override
    public void setFuel(int fuel) {
        Preconditions.checkArgument(fuel >= 0, "ticks cannot be negative");
        getHandle().fuel = fuel;
    }

    @Override
    public String toString() {
        return "CraftMinecartFurnace";
    }
}
