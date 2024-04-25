package com.mohistmc.mohist.bukkit.entity;

import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;

public class MohistModsEntity extends CraftEntity {

    public MohistModsEntity(CraftServer server, net.minecraft.world.entity.Entity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsEntity{" + this.getType() + '}';
    }
}
