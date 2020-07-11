package org.bukkit.entity;

import java.util.UUID;

public interface AnimalTamer {

    /**
     * This is the name of the specified AnimalTamer.
     *
     * @return The name to reference on tamed animals or null if a name cannot be obtained
     */
    String getName();

    /**
     * This is the UUID of the specified AnimalTamer.
     *
     * @return The UUID to reference on tamed animals
     */
    UUID getUniqueId();
}
