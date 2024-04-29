package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderPearl;

public class CraftEnderPearl extends CraftThrowableProjectile implements EnderPearl {
    public CraftEnderPearl(CraftServer server, net.minecraft.world.entity.projectile.ThrownEnderpearl entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.ThrownEnderpearl getHandle() {
        return (net.minecraft.world.entity.projectile.ThrownEnderpearl) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderPearl";
    }
}
