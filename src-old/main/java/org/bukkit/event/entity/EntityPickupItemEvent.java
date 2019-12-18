package org.bukkit.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Thrown when a entity picks an item up from the ground
 */
public class EntityPickupItemEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Item item;
    private final int remaining;
    private boolean cancel = false;

    public EntityPickupItemEvent(final LivingEntity entity, final Item item, final int remaining) {
        super(entity);
        this.item = item;
        this.remaining = remaining;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Gets the Item picked up by the entity.
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
