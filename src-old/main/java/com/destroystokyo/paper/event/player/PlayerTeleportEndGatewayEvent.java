package com.destroystokyo.paper.event.player;

import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Fired when a teleport is triggered for an End Gateway
 */
public class PlayerTeleportEndGatewayEvent extends PlayerTeleportEvent {
    private final EndGateway gateway;

    public PlayerTeleportEndGatewayEvent(Player player, Location from, Location to, EndGateway gateway) {
        super(player, from, to, PlayerTeleportEvent.TeleportCause.END_GATEWAY);
        this.gateway = gateway;
    }

    /**
     * The gateway triggering the teleport
     *
     * @return EndGateway used
     */
    public EndGateway getGateway() {
        return gateway;
    }
}
