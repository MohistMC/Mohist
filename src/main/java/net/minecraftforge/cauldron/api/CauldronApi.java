package net.minecraftforge.cauldron.api;

import net.minecraftforge.cauldron.api.inventory.BukkitOreDictionary;

/**
 * Represents the Bukkit plugin interface to Forge features.
 */
public interface CauldronApi {
    /**
     * Get the ore dictionary interface.
     *
     * @return ore dictionary interface
     */
    public BukkitOreDictionary getOreDictionary();

    /**
     * Get the fishing interface.
     *
     * @return the fishing interface
     */
    public Fishing getFishingInterface();
}
