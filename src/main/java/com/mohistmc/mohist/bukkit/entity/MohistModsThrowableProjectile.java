package com.mohistmc.mohist.bukkit.entity;

import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftThrowableProjectile;

public class MohistModsThrowableProjectile extends CraftThrowableProjectile {

    public MohistModsThrowableProjectile(CraftServer server, ThrowableItemProjectile entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsThrowableProjectile{" + getType() + '}';
    }
}
