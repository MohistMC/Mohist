package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTeleportEvent;

/**
 * Fired any time an entity attempts to teleport in an end gateway
 */
public class EntityTeleportEndGatewayEvent extends EntityTeleportEvent {

    private final EndGateway gateway;

    public EntityTeleportEndGatewayEvent(Entity what, Location from, Location to, EndGateway gateway) {
        super(what, from, to);
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
