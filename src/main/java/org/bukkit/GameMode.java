package org.bukkit;

import com.google.common.collect.Maps;
import org.bukkit.entity.HumanEntity;

import java.util.Map;

/**
 * Represents the various type of game modes that {@link HumanEntity}s may
 * have
 */
public enum GameMode {
    /**
     * Creative mode may fly, build instantly, become invulnerable and create
     * free items.
     */
    CREATIVE(1),

    /**
     * Survival mode is the "normal" gameplay type, with no special features.
     */
    SURVIVAL(0),

    /**
     * Adventure mode cannot break blocks without the correct tools.
     */
    ADVENTURE(2),

    /**
     * Spectator mode cannot interact with the world in anyway and is
     * invisible to normal players. This grants the player the
     * ability to no-clip through the world.
     */
    SPECTATOR(3);

    private final static Map<Integer, GameMode> BY_ID = Maps.newHashMap();

    static {
        for (GameMode mode : values()) {
            BY_ID.put(mode.getValue(), mode);
        }
    }

    private final int value;

    GameMode(final int value) {
        this.value = value;
    }

    /**
     * Gets the GameMode represented by the specified value
     *
     * @param value Value to check
     * @return Associative {@link GameMode} with the given value, or null if
     * it doesn't exist
     * @deprecated Magic value
     */

    public static GameMode getByValue(final int value) {
        return BY_ID.get(value);
    }

    /**
     * Gets the mode value associated with this GameMode
     *
     * @return An integer value of this gamemode
     * @deprecated Magic value
     */

    public int getValue() {
        return value;
    }
}
