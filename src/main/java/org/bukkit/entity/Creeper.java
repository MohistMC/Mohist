package org.bukkit.entity;

/**
 * Represents a Creeper
 */
public interface Creeper extends Monster {

    /**
     * Checks if this Creeper is powered (Electrocuted)
     *
     * @return true if this creeper is powered
     */
    boolean isPowered();

    /**
     * Sets the Powered status of this Creeper
     *
     * @param value New Powered status
     */
    void setPowered(boolean value);

    /**
     * Get the maximum fuse ticks for this Creeper, where the maximum ticks
     * is the amount of time in which a creeper is allowed to be in the
     * primed state before exploding.
     *
     * @return the maximum fuse ticks
     */
    int getMaxFuseTicks();

    /**
     * Set the maximum fuse ticks for this Creeper, where the maximum ticks
     * is the amount of time in which a creeper is allowed to be in the
     * primed state before exploding.
     *
     * @param ticks the new maximum fuse ticks
     */
    void setMaxFuseTicks(int ticks);

    /**
     * Get the explosion radius in which this Creeper's explosion will affect.
     *
     * @return the explosion radius
     */
    int getExplosionRadius();

    /**
     * Set the explosion radius in which this Creeper's explosion will affect.
     *
     * @param radius the new explosion radius
     */
    void setExplosionRadius(int radius);
}
