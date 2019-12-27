package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.GolemEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Golem;

public class CraftGolem extends CraftCreature implements Golem {
    public CraftGolem(CraftServer server, GolemEntity entity) {
        super(server, entity);
    }

    @Override
    public GolemEntity getHandle() {
        return (GolemEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftGolem";
    }
}
