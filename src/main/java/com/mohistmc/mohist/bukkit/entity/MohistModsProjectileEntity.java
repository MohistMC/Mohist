package com.mohistmc.mohist.bukkit.entity;

import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftProjectile;

public class MohistModsProjectileEntity extends CraftProjectile {

    public MohistModsProjectileEntity(CraftServer server, Projectile entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsProjectileEntity{" + getType() + '}';
    }
}

