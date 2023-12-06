package org.bukkit.craftbukkit.v1_20_R3.entity;

import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.entity.Husk;

public class CraftHusk extends CraftZombie implements Husk {

    public CraftHusk(CraftServer server, net.minecraft.world.entity.monster.Husk entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftHusk";
    }
}
