package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull extends CraftFireball implements WitherSkull {
    public CraftWitherSkull(CraftServer server, net.minecraft.entity.projectile.EntityWitherSkull entity) {
        super(server, entity);
    }

    @Override
    public void setCharged(boolean charged) {
        getHandle().setInvulnerable(charged);
    }

    @Override
    public boolean isCharged() {
        return getHandle().isInvulnerable();
    }

    @Override
    public net.minecraft.entity.projectile.EntityWitherSkull getHandle() {
        return (net.minecraft.entity.projectile.EntityWitherSkull) entity;
    }

    @Override
    public String toString() {
        return "CraftWitherSkull";
    }

    public EntityType getType() {
        return EntityType.WITHER_SKULL;
    }
}
