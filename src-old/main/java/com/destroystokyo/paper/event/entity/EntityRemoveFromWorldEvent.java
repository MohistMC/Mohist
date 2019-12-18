package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Fired any time an entity is being removed from a world for any reason
 */
public class EntityRemoveFromWorldEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();

    public EntityRemoveFromWorldEvent(Entity entity) {
        super(entity);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
