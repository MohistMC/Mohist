
package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a player related inventory event
 */
public class InventoryCloseEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();

    // Paper start
    private final Reason reason;
    @NotNull
    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        /**
         * Unknown reason
         */
        UNKNOWN,
        /**
         * Player is teleporting
         */
        TELEPORT,
        /**
         * Player is no longer permitted to use this inventory
         */
        CANT_USE,
        /**
         * The chunk the inventory was in was unloaded
         */
        UNLOADED,
        /**
         * Opening new inventory instead
         */
        OPEN_NEW,
        /**
         * Closed
         */
        PLAYER,
        /**
         * Closed due to disconnect
         */
        DISCONNECT,
        /**
         * The player died
         */
        DEATH,
        /**
         * Closed by Bukkit API
         */
        PLUGIN,
    }

    public InventoryCloseEvent(@NotNull InventoryView transaction) {
        this(transaction, Reason.UNKNOWN);
    }

    public InventoryCloseEvent(@NotNull InventoryView transaction, @NotNull Reason reason) {
        super(transaction);
        this.reason = reason;
        // Paper end
    }

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    @NotNull
    public final HumanEntity getPlayer() {
        return transaction.getPlayer();
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
