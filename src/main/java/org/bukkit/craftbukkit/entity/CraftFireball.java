package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CraftFireball extends AbstractProjectile implements Fireball {
    public CraftFireball(CraftServer server, ExplosiveProjectileEntity entity) {
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
            getHandle().owner = ((CraftLivingEntity) shooter).getHandle();
        } else {
            getHandle().owner = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public Vector getDirection() {
        return new Vector(getHandle().posX, getHandle().posY, getHandle().posZ);
    }

    @Override
    public void setDirection(Vector direction) {
        Validate.notNull(direction, "Direction can not be null");
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();
        double magnitude = (double) MathHelper.sqrt(x * x + y * y + z * z);
        getHandle().posX = x / magnitude * 0.1D;
        getHandle().posY = y / magnitude * 0.1D;
        getHandle().posZ = z / magnitude * 0.1D;
    }

    @Override
    public ExplosiveProjectileEntity getHandle() {
        return (ExplosiveProjectileEntity) entity;
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
