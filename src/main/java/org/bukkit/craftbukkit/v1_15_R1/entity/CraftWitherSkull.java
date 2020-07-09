package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.projectile.WitherSkullEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull extends CraftFireball implements WitherSkull {
    public CraftWitherSkull(CraftServer server, WitherSkullEntity entity) {
        super(server, entity);
    }

    @Override
    public void setCharged(boolean charged) {
        getHandle().setSkullInvulnerable(charged);
    }

    @Override
    public boolean isCharged() {
        return getHandle().isSkullInvulnerable();
    }

    @Override
    public WitherSkullEntity getHandle() {
        return (WitherSkullEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWitherSkull";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER_SKULL;
    }
}
