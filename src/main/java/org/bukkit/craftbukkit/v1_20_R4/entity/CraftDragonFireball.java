package org.bukkit.craftbukkit.v1_20_R4.entity;

import org.bukkit.craftbukkit.v1_20_R4.CraftServer;
import org.bukkit.entity.DragonFireball;

public class CraftDragonFireball extends CraftFireball implements DragonFireball {
    public CraftDragonFireball(CraftServer server, net.minecraft.world.entity.projectile.DragonFireball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftDragonFireball";
    }
}
