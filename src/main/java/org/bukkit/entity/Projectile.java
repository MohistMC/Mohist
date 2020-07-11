package org.bukkit.entity;

import org.bukkit.projectiles.ProjectileSource;

/**
 * Represents a shootable entity.
 */
public interface Projectile extends Entity {

    /**
     * Retrieve the shooter of this projectile.
     *
     * @return the {@link ProjectileSource} that shot this projectile
     */
    ProjectileSource getShooter();

    /**
     * Set the shooter of this projectile.
     *
     * @param source the {@link ProjectileSource} that shot this projectile
     */
    void setShooter(ProjectileSource source);

    /**
     * Determine if this projectile should bounce or not when it hits.
     * <p>
     * If a small fireball does not bounce it will set the target on fire.
     *
     * @return true if it should bounce.
     */
    boolean doesBounce();

    /**
     * Set whether or not this projectile should bounce or not when it hits
     * something.
     *
     * @param doesBounce whether or not it should bounce.
     */
    void setBounce(boolean doesBounce);
}
