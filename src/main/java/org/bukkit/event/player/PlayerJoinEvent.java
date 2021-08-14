package org.bukkit.event.player;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a player joins a server
 */
public class PlayerJoinEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private String joinMessage;
    private final World world;

    public PlayerJoinEvent(final Player playerJoined, final String joinMessage) {
        super(playerJoined);
        this.joinMessage = joinMessage;
        this.world = playerJoined.getWorld();
    }

    public PlayerJoinEvent(final Player who, final World world) {
        super(who);
        this.joinMessage = "";
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the join message to send to all online players
     *
     * @return string join message
     */
    public String getJoinMessage() {
        return joinMessage;
    }

    /**
     * Sets the join message to send to all online players
     *
     * @param joinMessage join message
     */
    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
