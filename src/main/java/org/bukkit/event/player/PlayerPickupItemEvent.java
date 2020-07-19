package org.bukkit.event.player;

import org.bukkit.Warning;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPickupItemEvent;

/**
 * Thrown when a player picks an item up from the ground
 *
 * @deprecated {@link EntityPickupItemEvent}
 */
@Warning(false)
public class PlayerPickupItemEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Item item;
    private final int remaining;
    private boolean cancel = false;

    public PlayerPickupItemEvent(final Player player, final Item item, final int remaining) {
        super(player);
        this.item = item;
        this.remaining = remaining;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the Item picked up by the player.
     *
     * @return Item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the amount remaining on the ground, if any
     *
     * @return amount remaining on the ground
     */
    public int getRemaining() {
        return remaining;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
