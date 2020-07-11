package org.bukkit.entity;

import org.bukkit.util.Vector;

/**
 * Represents a Fireball.
 */
public interface Fireball extends Projectile, Explosive {

    /**
     * Retrieve the direction this fireball is heading toward
     *
     * @return the direction
     */
    Vector getDirection();

    /**
     * Fireballs fly straight and do not take setVelocity(...) well.
     *
     * @param direction the direction this fireball is flying toward
     */
    void setDirection(Vector direction);

}
