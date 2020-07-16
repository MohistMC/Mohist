package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.thrown.ThrownEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().owner = (LivingEntity) ((CraftLivingEntity) shooter).entity;
            getHandle().ownerUuid = ((CraftLivingEntity) shooter).getUniqueId();
        } else {
            getHandle().owner = null;
            getHandle().ownerUuid = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public ThrownEntity getHandle() {
        return (ThrownEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }
}
