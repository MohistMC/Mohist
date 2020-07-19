package org.bukkit.advancement;

import org.bukkit.Keyed;

import java.util.Collection;

/**
 * Represents an advancement that may be awarded to a player. This class is not
 * reference safe as the underlying advancement may be reloaded.
 */
public interface Advancement extends Keyed {

    /**
     * Get all the criteria present in this advancement.
     *
     * @return a unmodifiable copy of all criteria
     */
    Collection<String> getCriteria();
}
