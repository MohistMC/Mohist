package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds information for player teleport events
 */
public class PlayerTeleportEvent extends PlayerMoveEvent {
    private static final HandlerList handlers = new HandlerList();
    private TeleportCause cause = TeleportCause.UNKNOWN;

    public PlayerTeleportEvent(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to) {
        super(player, from, to);
    }

    public PlayerTeleportEvent(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to, @NotNull final TeleportCause cause) {
        this(player, from, to);

        this.cause = cause;
    }

    /**
     * Gets the cause of this teleportation event
     *
     * @return Cause of the event
     */
    @NotNull
    public TeleportCause getCause() {
        return cause;
    }

    public enum TeleportCause {
        /**
         * Indicates the teleporation was caused by a player throwing an Ender
         * Pearl
         */
        ENDER_PEARL,
        /**
         * Indicates the teleportation was caused by a player executing a
         * command
         */
        COMMAND,
        /**
         * Indicates the teleportation was caused by a plugin
         */
        PLUGIN,
        /**
         * Indicates the teleportation was caused by a player entering a
         * Nether portal
         */
        NETHER_PORTAL,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * portal
         */
        END_PORTAL,
        // Cauldron start - added cause for mods
        /**
         * Indicates the teleportation was caused by a player entering a
         * Mod portal
         */
        MOD,
        // Cauldron end
        DEATH, // KCauldron
        /**
         * Indicates the teleportation was caused by a player teleporting to a
         * Entity/Player via the spectator menu
         */
        SPECTATE,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * gateway
         */
        END_GATEWAY,
        /**
         * Indicates the teleportation was caused by a player consuming chorus
         * fruit
         */
        CHORUS_FRUIT,
        /**
         * Indicates the teleportation was caused by an event not covered by
         * this enum
         */
        UNKNOWN;

        public boolean isPortal() {
            switch(this) {
                case NETHER_PORTAL:
                case END_PORTAL:
                    return true;
            }
            return false;
        }
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
