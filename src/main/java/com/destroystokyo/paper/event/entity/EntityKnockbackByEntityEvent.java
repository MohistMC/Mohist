package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.util.Vector;

/**
 * Fired when an Entity is knocked back by the hit of another Entity. The acceleration
 * vector can be modified. If this event is cancelled, the entity is not knocked back.
 *
 */
public class EntityKnockbackByEntityEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Entity hitBy;
    private final float knockbackStrength;
    private final Vector acceleration;
    private boolean cancelled = false;

    public EntityKnockbackByEntityEvent(LivingEntity entity, Entity hitBy, float knockbackStrength, Vector acceleration) {
        super(entity);
        this.hitBy = hitBy;
        this.knockbackStrength = knockbackStrength;
        this.acceleration = acceleration;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * @return the entity which was knocked back
     */
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    /**
     * @return the original knockback strength.
     */
    public float getKnockbackStrength() {
        return knockbackStrength;
    }

    /**
     * @return the Entity which hit
     */
    public Entity getHitBy() {
        return hitBy;
    }

    /**
     * @return the acceleration that will be applied
     */
    public Vector getAcceleration() {
        return acceleration;
    }
}
