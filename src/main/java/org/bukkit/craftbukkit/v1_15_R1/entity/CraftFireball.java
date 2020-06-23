package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CraftFireball extends AbstractProjectile implements Fireball {
    public CraftFireball(CraftServer server, DamagingProjectileEntity entity) {
        super(server, entity);
    }

    @Override
    public float getYield() {
        return getHandle().bukkitYield;
    }

    @Override
    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    @Override
    public void setYield(float yield) {
        getHandle().bukkitYield = yield;
    }

    @Override
    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().shootingEntity = ((CraftLivingEntity) shooter).getHandle();
        } else {
            getHandle().shootingEntity = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public Vector getDirection() {
        return new Vector(getHandle().accelerationX, getHandle().accelerationY, getHandle().accelerationZ);
    }

    @Override
    public void setDirection(Vector direction) {
        Validate.notNull(direction, "Direction can not be null");
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();
        double magnitude = (double) MathHelper.sqrt(x * x + y * y + z * z);
        getHandle().accelerationX = x / magnitude * 0.1D;
        getHandle().accelerationY = y / magnitude * 0.1D;
        getHandle().accelerationZ = z / magnitude * 0.1D;
    }

    @Override
    public DamagingProjectileEntity getHandle() {
        return (DamagingProjectileEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
