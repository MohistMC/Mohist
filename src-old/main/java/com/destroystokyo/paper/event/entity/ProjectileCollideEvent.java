package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Called when an projectile collides with an entity
 * <p>
 * This event is called <b>before</b> {@link org.bukkit.event.entity.EntityDamageByEntityEvent}, and cancelling it will allow the projectile to continue flying
 */
public class ProjectileCollideEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();
    private final Entity collidedWith;
    private boolean cancelled = false;

    public ProjectileCollideEvent(Projectile what, Entity collidedWith) {
        super(what);
        this.collidedWith = collidedWith;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * Get the entity the projectile collided with
     *
     * @return the entity collided with
     */
    public Entity getCollidedWith() {
        return collidedWith;
    }

    /**
     * Get the projectile that collided
     *
     * @return the projectile that collided
     */
    public Projectile getEntity() {
        return (Projectile) super.getEntity();
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
