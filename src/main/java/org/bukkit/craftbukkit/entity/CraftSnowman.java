package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Snowman;

public class CraftSnowman extends CraftGolem implements Snowman {
    public CraftSnowman(CraftServer server, net.minecraft.world.entity.animal.SnowGolem entity) {
        super(server, entity);
    }

    @Override
    public boolean isDerp() {
        return !getHandle().hasPumpkin();
    }

    @Override
    public void setDerp(boolean derpMode) {
        getHandle().setPumpkin(!derpMode);
    }

    @Override
    public net.minecraft.world.entity.animal.SnowGolem getHandle() {
        return (net.minecraft.world.entity.animal.SnowGolem) entity;
    }

    @Override
    public String toString() {
        return "CraftSnowman";
    }
}
