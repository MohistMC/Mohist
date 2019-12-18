package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block is destroyed as a result of being burnt by fire.
 * <p>
 * If a Block Burn event is cancelled, the block will not be destroyed as a
 * result of being burnt by fire.
 */
public class BlockBurnEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block ignitingBlock;
    private boolean cancelled;

    public BlockBurnEvent(final Block block) {
        this(block, null);
    }

    public BlockBurnEvent(final Block block, final Block ignitingBlock) {
        super(block);
        this.ignitingBlock = ignitingBlock;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the block which ignited this block.
     *
     * @return The Block that ignited and burned this block, or null if no
     * source block exists
     */
    public Block getIgnitingBlock() {
        return ignitingBlock;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
