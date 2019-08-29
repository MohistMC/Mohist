package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.projectile.EntityWitherSkull;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull extends CraftFireball implements WitherSkull {
    public CraftWitherSkull(CraftServer server, EntityWitherSkull entity) {
        super(server, entity);
    }

    @Override
    public boolean isCharged() {
        return getHandle().isInvulnerable();
    }

    @Override
    public void setCharged(boolean charged) {
        getHandle().setInvulnerable(charged);
    }

    @Override
    public EntityWitherSkull getHandle() {
        return (EntityWitherSkull) entity;
    }

    @Override
    public String toString() {
        return "CraftWitherSkull";
    }

    public EntityType getType() {
        return EntityType.WITHER_SKULL;
    }
}
