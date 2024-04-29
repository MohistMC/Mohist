package com.mohistmc.mohist.bukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;

public class MohistModsEntity extends CraftEntity {

    public MohistModsEntity(CraftServer server, net.minecraft.world.entity.Entity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsEntity{" + this.getType() + '}';
    }
}
