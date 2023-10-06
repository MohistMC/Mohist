package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.EntityArrow;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;

public class CraftArrow extends AbstractProjectile implements Arrow {

    public CraftArrow(CraftServer server, EntityArrow entity) {
        super(server, entity);
    }

    public void setKnockbackStrength(int knockbackStrength) {
        Validate.isTrue(knockbackStrength >= 0, "Knockback cannot be negative");
        getHandle().setKnockbackStrength(knockbackStrength);
    }

    public int getKnockbackStrength() {
        return getHandle().knockbackStrength;
    }

    public boolean isCritical() {
        return getHandle().getIsCritical();
    }

    public void setCritical(boolean critical) {
        getHandle().setIsCritical(critical);
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            getHandle().shootingEntity = ((CraftLivingEntity) shooter).getHandle();
        } else {
            getHandle().shootingEntity = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public EntityArrow getHandle() {
        return (EntityArrow) entity;
    }

    @Override
    public String toString() {
        return "CraftArrow";
    }

    public EntityType getType() {
        return EntityType.ARROW;
    }

    @Deprecated
    public LivingEntity _INVALID_getShooter() {
        if (getHandle().shootingEntity == null) {
            return null;
        }
        return (LivingEntity) getHandle().shootingEntity.getBukkitEntity();
    }

    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter) {
        getHandle().shootingEntity = ((CraftLivingEntity) shooter).getHandle();
    }

    // Spigot start
    private final Arrow.Spigot spigot = new Arrow.Spigot()
    {
        @Override
        public double getDamage()
        {
            return getHandle().getDamage();
        }

        @Override
        public void setDamage(double damage)
        {
            getHandle().setDamage( damage );
        }
    };

    public Arrow.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
