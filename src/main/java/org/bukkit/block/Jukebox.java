package org.bukkit.block;

import org.bukkit.Material;

/**
 * Represents a captured state of a jukebox.
 */
public interface Jukebox extends BlockState {

    /**
     * Gets the record being played.
     *
     * @return The record Material, or AIR if none is playing
     */
    Material getPlaying();

    /**
     * Checks if the jukebox is playing a record.
     *
     * @return True if there is a record playing
     */
    boolean isPlaying();

    /**
     * Sets the record being played.
     *
     * @param record The record Material, or null/AIR to stop playing
     */
    void setPlaying(Material record);

    /**
     * Stops the jukebox playing and ejects the current record.
     * <p>
     * If the block represented by this state is no longer a jukebox, this will
     * do nothing and return false.
     *
     * @return True if a record was ejected; false if there was none playing
     * @throws IllegalStateException if this block state is not placed
     */
    boolean eject();
}
