package com.destroystokyo.paper.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

/**
 * Represents information about a targeted block
 */
public class TargetBlockInfo {
    private final Block block;
    private final BlockFace blockFace;

    public TargetBlockInfo(@NotNull Block block, @NotNull BlockFace blockFace) {
        this.block = block;
        this.blockFace = blockFace;
    }

    /**
     * Get the block that is targeted
     *
     * @return Targeted block
     */
    @NotNull
    public Block getBlock() {
        return block;
    }

    /**
     * Get the targeted BlockFace
     *
     * @return Targeted blockface
     */
    @NotNull
    public BlockFace getBlockFace() {
        return blockFace;
    }

    /**
     * Get the relative Block to the targeted block on the side it is targeted at
     *
     * @return Block relative to targeted block
     */
    @NotNull
    public Block getRelativeBlock() {
        return block.getRelative(blockFace);
    }

    public enum FluidMode {
        NEVER,
        SOURCE_ONLY,
        ALWAYS
    }
}
