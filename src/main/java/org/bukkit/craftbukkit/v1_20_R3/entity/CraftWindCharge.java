package org.bukkit.craftbukkit.v1_20_R3.entity;

import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.entity.WindCharge;

public class CraftWindCharge extends CraftFireball implements WindCharge {
    public CraftWindCharge(CraftServer server, net.minecraft.world.entity.projectile.WindCharge entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftWindCharge";
    }
}
