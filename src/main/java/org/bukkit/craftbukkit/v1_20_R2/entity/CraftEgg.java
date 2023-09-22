package org.bukkit.craftbukkit.v1_20_R2.entity;

import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.Egg;

public class CraftEgg extends CraftThrowableProjectile implements Egg {
    public CraftEgg(CraftServer server, net.minecraft.world.entity.projectile.ThrownEgg entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.ThrownEgg getHandle() {
        return (net.minecraft.world.entity.projectile.ThrownEgg) entity;
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }
}
