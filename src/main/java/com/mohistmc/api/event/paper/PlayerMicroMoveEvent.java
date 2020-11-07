package com.mohistmc.api.event.paper;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * This event is fired for player movements that are too small to track with PlayerMoveEvent.
 *
 * When used in combination with PlayerMoveEvent, it is possible to keep track of all
 * PacketPlayInFlying movements. This can be particularly useful for anti-cheat plugins.
 *
 * Please note this event is not intended for casual use. Plugins that casually use this event
 * may cause significant overhead depending on handler logic.
 */
public class PlayerMicroMoveEvent extends PlayerMoveEvent {
    private static final HandlerList handlerList = new HandlerList();

    public PlayerMicroMoveEvent(Player player, Location from, Location to) {
        super(player, from, to);
    }

    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
