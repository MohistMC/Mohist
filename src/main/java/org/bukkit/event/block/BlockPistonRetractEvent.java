package org.bukkit.event.block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * Called when a piston retracts
 */
public class BlockPistonRetractEvent extends BlockPistonEvent {
    private static final HandlerList handlers = new HandlerList();
    private final List<Block> blocks;

    public BlockPistonRetractEvent(final Block block, final List<Block> blocks, final BlockFace direction) {
        super(block, direction);

        this.blocks = blocks;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the location where the possible moving block might be if the
     * retracting piston is sticky.
     *
     * @return The possible location of the possibly moving block.
     */
    public Location getRetractLocation() {
        return getBlock().getRelative(getDirection(), 2).getLocation();
    }

    /**
     * Get an immutable list of the blocks which will be moved by the
     * extending.
     *
     * @return Immutable list of the moved blocks.
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
