package org.bukkit.craftbukkit.entity;


import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class CraftProjectile extends AbstractProjectile implements Projectile { // Cauldron - concrete
    public CraftProjectile(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
        }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().thrower = (net.minecraft.entity.EntityLivingBase) ((CraftLivingEntity) shooter).entity;
            if (shooter instanceof CraftHumanEntity) {
                getHandle().throwerName = ((CraftHumanEntity) shooter).getName();
            }
        } else {
            getHandle().thrower = null;
            getHandle().throwerName = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public net.minecraft.entity.projectile.EntityThrowable getHandle() {
        return (net.minecraft.entity.projectile.EntityThrowable) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }

    // Cauldron start
    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
    // Cauldron end

    @Deprecated
    public LivingEntity _INVALID_getShooter() {
        if (getHandle().thrower == null) {
            return null;
        }
        return (LivingEntity) getHandle().thrower.getBukkitEntity();
    }

    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter) {
        if (shooter == null) {
            return;
        }
        getHandle().thrower = ((CraftLivingEntity) shooter).getHandle();
        if (shooter instanceof CraftHumanEntity) {
            getHandle().throwerName = ((CraftHumanEntity) shooter).getName();
        }
    }
}
