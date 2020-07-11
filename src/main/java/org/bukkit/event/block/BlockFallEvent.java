package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockFallEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private FallingBlock fallingBlock;
    private boolean cancelled = false;

    public BlockFallEvent(final Block block, final FallingBlock fallingBlock) {
        super(block);
        this.fallingBlock = fallingBlock;
    }

    public FallingBlock getEntity() {
        return fallingBlock;
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

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
