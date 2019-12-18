package org.bukkit.event.hanging;

import org.bukkit.entity.Hanging;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Triggered when a hanging entity is removed
 */
public class HangingBreakEvent extends HangingEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final HangingBreakEvent.RemoveCause cause;
    private boolean cancelled;

    public HangingBreakEvent(final Hanging hanging, final HangingBreakEvent.RemoveCause cause) {
        super(hanging);
        this.cause = cause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the cause for the hanging entity's removal
     *
     * @return the RemoveCause for the hanging entity's removal
     */
    public HangingBreakEvent.RemoveCause getCause() {
        return cause;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * An enum to specify the cause of the removal
     */
    public enum RemoveCause {
        /**
         * Removed by an entity
         */
        ENTITY,
        /**
         * Removed by an explosion
         */
        EXPLOSION,
        /**
         * Removed by placing a block on it
         */
        OBSTRUCTION,
        /**
         * Removed by destroying the block behind it, etc
         */
        PHYSICS,
        /**
         * Removed by an uncategorised cause
         */
        DEFAULT,
    }
}
