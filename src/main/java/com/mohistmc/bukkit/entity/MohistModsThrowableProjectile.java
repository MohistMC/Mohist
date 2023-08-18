package com.mohistmc.bukkit.entity;

import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftThrowableProjectile;

public class MohistModsThrowableProjectile extends CraftThrowableProjectile {

    public MohistModsThrowableProjectile(CraftServer server, ThrowableItemProjectile entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsThrowableProjectile{" + getType() + '}';
    }
}
