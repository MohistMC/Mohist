package com.mohistmc.bukkit.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftMinecartContainer;

public class MohistModsMinecartContainer extends CraftMinecartContainer {

    public MohistModsMinecartContainer(CraftServer server, AbstractMinecartContainer entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsMinecartContainer{" + getType() + '}';
    }
}
