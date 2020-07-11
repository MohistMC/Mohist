package org.bukkit.entity;

/**
 * A representation of an explosive entity
 */
public interface Explosive extends Entity {

    /**
     * Return the radius or yield of this explosive's explosion
     *
     * @return the radius of blocks affected
     */
    float getYield();

    /**
     * Set the radius affected by this explosive's explosion
     *
     * @param yield The explosive yield
     */
    void setYield(float yield);

    /**
     * Set whether or not this explosive's explosion causes fire
     *
     * @param isIncendiary Whether it should cause fire
     */
    void setIsIncendiary(boolean isIncendiary);

    /**
     * Return whether or not this explosive creates a fire when exploding
     *
     * @return true if the explosive creates fire, false otherwise
     */
    boolean isIncendiary();
}
