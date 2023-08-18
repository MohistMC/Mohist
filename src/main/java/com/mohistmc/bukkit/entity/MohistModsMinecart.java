package com.mohistmc.bukkit.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftMinecart;


public class MohistModsMinecart extends CraftMinecart {

    public MohistModsMinecart(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsMinecart{" + getType() + '}';
    }
}
