package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an Experience Orb.
 */
public interface ExperienceOrb extends Entity {

    /**
     * Gets how much experience is contained within this orb
     *
     * @return Amount of experience
     */
    public int getExperience();

    /**
     * Sets how much experience is contained within this orb
     *
     * @param value Amount of experience
     */
    public void setExperience(int value);
}
