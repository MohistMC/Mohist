package org.bukkit.entity;

/**
 * Represents a wither skull {@link Fireball}.
 */
public interface WitherSkull extends Fireball {

    /**
     * Gets whether or not the wither skull is charged.
     *
     * @return whether the wither skull is charged
     */
    public boolean isCharged();

    /**
     * Sets the charged status of the wither skull.
     *
     * @param charged whether it should be charged
     */
    public void setCharged(boolean charged);
}
