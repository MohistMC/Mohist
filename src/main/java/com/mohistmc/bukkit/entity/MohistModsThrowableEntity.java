package com.mohistmc.bukkit.entity;

import net.minecraft.world.entity.projectile.ThrowableProjectile;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftProjectile;

public class MohistModsThrowableEntity extends CraftProjectile {
    public MohistModsThrowableEntity(CraftServer server, ThrowableProjectile entity) {
        super(server, entity);
    }

    @Override
    public ThrowableProjectile getHandle() {
        return (ThrowableProjectile) entity;
    }

    @Override
    public String toString() {
        return "MohistModsThrowableEntity{" + getType() + '}';
    }
}
