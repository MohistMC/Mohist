package io.papermc.paper.event.entity;

import org.bukkit.entity.PufferFish;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called just before a {@link PufferFish} inflates or deflates.
 */
public class PufferFishStateChangeEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private int newPuffState;

    public PufferFishStateChangeEvent(@NotNull PufferFish entity, int newPuffState) {
        super(entity);
        this.newPuffState = newPuffState;
    }

    @NotNull
    @Override
    public PufferFish getEntity() {
        return (PufferFish) entity;
    }

    /**
     * Get the <strong>new</strong> puff state of the {@link PufferFish}.
     * <p>
     * This is what the {@link PufferFish}'s new puff state will be after this event if it isn't cancelled.<br>
     * Refer to {@link PufferFish#getPuffState()} to get the current puff state.
     * @return The <strong>new</strong> puff state, 0 being not inflated, 1 being slightly inflated and 2 being fully inflated
     */
    public int getNewPuffState() {
        return this.newPuffState;
    }

    /**
     * Get if the {@link PufferFish} is going to inflate.
     * @return If its going to inflate
     */
    public boolean isInflating() {
        return getNewPuffState() > getEntity().getPuffState();
    }

    /**
     * Get if the {@link PufferFish} is going to deflate.
     * @return If its going to deflate
     */
    public boolean isDeflating() {
        return getNewPuffState() < getEntity().getPuffState();
    }

    /**
     * Set whether or not to cancel the {@link PufferFish} (in/de)flating.
     *
     * @param cancel true if you wish to cancel the (in/de)flation
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
