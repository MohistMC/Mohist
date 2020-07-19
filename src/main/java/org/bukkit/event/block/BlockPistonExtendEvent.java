package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Called when a piston extends
 */
public class BlockPistonExtendEvent extends BlockPistonEvent {
    private static final HandlerList handlers = new HandlerList();
    private final int length;
    private List<Block> blocks;

    public BlockPistonExtendEvent(final Block block, final int length, final BlockFace direction) {
        super(block, direction);

        this.length = length;
    }

    public BlockPistonExtendEvent(final Block block, final List<Block> blocks, final BlockFace direction) {
        super(block, direction);

        this.length = blocks.size();
        this.blocks = blocks;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the amount of blocks which will be moved while extending.
     *
     * @return the amount of moving blocks
     * @deprecated slime blocks make the value of this method
     * inaccurate due to blocks being pushed at the side
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Get an immutable list of the blocks which will be moved by the
     * extending.
     *
     * @return Immutable list of the moved blocks.
     */
    public List<Block> getBlocks() {
        if (blocks == null) {
            ArrayList<Block> tmp = new ArrayList<>();
            for (int i = 0; i < this.getLength(); i++) {
                tmp.add(block.getRelative(getDirection(), i + 1));
            }
            blocks = Collections.unmodifiableList(tmp);
        }
        return blocks;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
