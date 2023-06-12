package org.bukkit;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Represents a fluid type.
 */
public enum Fluid implements Keyed {

    /**
     * Stationary water.
     */
    WATER,
    /**
     * Flowing water.
     */
    FLOWING_WATER,
    /**
     * Stationary lava.
     */
    LAVA,
    /**
     * Flowing lava.
     */
    FLOWING_LAVA;

    private final NamespacedKey key;

    private Fluid() {
        this.key = NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT));
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
