package org.bukkit.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ItemMergeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Item target;
    private boolean cancelled;

    public ItemMergeEvent(Item item, Item target) {
        super(item);
        this.target = target;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public Item getEntity() {
        return (Item) entity;
    }

    /**
     * Gets the Item entity the main Item is being merged into.
     *
     * @return The Item being merged with
     */
    public Item getTarget() {
        return target;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
