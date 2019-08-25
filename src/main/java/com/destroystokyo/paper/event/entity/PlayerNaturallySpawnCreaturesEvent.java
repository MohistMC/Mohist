package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Fired when the server is calculating what chunks to try to spawn monsters in every Monster Spawn Tick event
 */
public class PlayerNaturallySpawnCreaturesEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private byte radius;
    private boolean cancelled = false;

    public PlayerNaturallySpawnCreaturesEvent(Player player, byte radius) {
        super(player);
        this.radius = radius;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return The radius of chunks around this player to be included in natural spawn selection
     */
    public byte getSpawnRadius() {
        return radius;
    }

    /**
     * @param radius The radius of chunks around this player to be included in natural spawn selection
     */
    public void setSpawnRadius(byte radius) {
        this.radius = radius;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @return If this players chunks will be excluded from natural spawns
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @param cancel true if you wish to cancel this event, and not include this players chunks for natural spawning
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
