package com.mohistmc.mohist.bukkit.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecart;


public class MohistModsMinecart extends CraftMinecart {

    public MohistModsMinecart(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsMinecart{" + getType() + '}';
    }
}
