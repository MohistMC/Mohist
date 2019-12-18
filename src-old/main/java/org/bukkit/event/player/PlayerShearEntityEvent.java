package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player shears an entity
 */
public class PlayerShearEntityEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity what;
    private boolean cancel;

    public PlayerShearEntityEvent(final Player who, final Entity what) {
        super(who);
        this.cancel = false;
        this.what = what;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the entity the player is shearing
     *
     * @return the entity the player is shearing
     */
    public Entity getEntity() {
        return what;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
