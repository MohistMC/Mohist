package net.minecraftforge.cauldron.api;

import net.minecraftforge.cauldron.api.inventory.BukkitOreDictionary;

/**
 * Represents the Bukkit plugin interface to Cauldron, for version and singleton handling
 */
public class Cauldron {
    private static CauldronApi instance;
    public static void setInterface(CauldronApi cauldron) {
        if (instance != null) {
            throw new IllegalStateException();
        }
        instance = cauldron;
    }

    /**
     * Gets the current CauldronApi singleton
     *
     * @return current instance of CauldronApi. will always be present.
     */
    public static CauldronApi getInterface() {
        return instance;
    }

    public static BukkitOreDictionary getOreDictionary() {
        return instance.getOreDictionary();
    }
}
