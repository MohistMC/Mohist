package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Raider;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link org.bukkit.entity.Raider} is revealed by a bell.
 */
public class BellRevealRaiderEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;
    private final Raider raider;

    public BellRevealRaiderEvent(@NotNull Block theBlock, @NotNull Entity raider) {
        super(theBlock);
        this.raider = (Raider) raider;
    }

    /**
     * Gets the raider that the bell revealed.
     *
     * @return The raider
     */
    @NotNull
    public Raider getEntity() {
        return raider;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This does not cancel the particle effects shown on the bell, only the entity.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
