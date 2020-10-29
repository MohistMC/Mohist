package org.bukkit.craftbukkit.v1_16_R2.entity;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, net.minecraft.entity.projectile.ProjectileEntity entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().setShooter(((CraftLivingEntity) shooter).entity);
        } else {
            getHandle().setShooter(null);
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public ProjectileEntity getHandle() {
        return (ProjectileEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }
}
