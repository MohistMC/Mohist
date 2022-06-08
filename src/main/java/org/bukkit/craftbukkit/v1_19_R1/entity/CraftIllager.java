package org.bukkit.craftbukkit.v1_19_R1.entity;

import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.entity.Illager;

public class CraftIllager extends CraftRaider implements Illager {

    public CraftIllager(CraftServer server, net.minecraft.world.entity.monster.AbstractIllager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.AbstractIllager getHandle() {
        return (net.minecraft.world.entity.monster.AbstractIllager) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftIllager";
    }
}
