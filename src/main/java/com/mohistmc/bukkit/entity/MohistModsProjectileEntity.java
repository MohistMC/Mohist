package com.mohistmc.bukkit.entity;

import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftProjectile;

public class MohistModsProjectileEntity extends CraftProjectile {

    public MohistModsProjectileEntity(CraftServer server, Projectile entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsProjectileEntity{" + getType() + '}';
    }
}

