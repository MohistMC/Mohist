package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
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
            getHandle().ownerId = ((CraftLivingEntity) shooter).getUniqueId();
        } else {
            getHandle().owner = null;
            getHandle().ownerId = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public ThrowableEntity getHandle() {
        return (ThrowableEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }
}
