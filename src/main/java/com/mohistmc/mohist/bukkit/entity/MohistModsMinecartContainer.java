package com.mohistmc.mohist.bukkit.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartContainer;

public class MohistModsMinecartContainer extends CraftMinecartContainer {

    public MohistModsMinecartContainer(CraftServer server, AbstractMinecartContainer entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsMinecartContainer{" + getType() + '}';
    }
}
