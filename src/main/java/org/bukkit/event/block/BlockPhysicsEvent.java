package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Thrown when a block physics check is called
 */
public class BlockPhysicsEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final int changed;
    private boolean cancel = false;
    // Paper start - add source block
    private int sourceX;
    private int sourceY;
    private int sourceZ;
    private Block sourceBlock;

    /**
     *
     * @deprecated Magic value
     * @param block the block involved in this event
     * @param changed the changed block's type id
     * @param sourceX the x of the source block
     * @param sourceY the y of the source block
     * @param sourceZ the z of the source block
     */
    @Deprecated
    public BlockPhysicsEvent(final Block block, final int changed, final int sourceX, final int sourceY, final int sourceZ) {
        this(block, changed);
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
        this.sourceBlock = null;
    }

    /**
     * Gets the source block, causing this event
     *
     * @return Source block
     */
    public Block getSourceBlock() {
        return sourceBlock == null ? (sourceBlock = block.getWorld().getBlockAt(sourceX, sourceY, sourceZ)) : sourceBlock;
    }
    // Paper end
    /**
     *
     * @deprecated Magic value
     * @param block the block involved in this event
     * @param changed the changed block's type id
     */
    public BlockPhysicsEvent(final Block block, final int changed) {
        super(block);
        this.changed = changed;
        this.sourceBlock = block; // Paper - add source block
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the type of block that changed, causing this event
     *
     * @return Changed block's type id
     * @deprecated Magic value
     */
    public int getChangedTypeId() {
        return changed;
    }

    /**
     * Gets the type of block that changed, causing this event
     *
     * @return Changed block's type
     */
    public Material getChangedType() {
        return Material.getBlockMaterial(changed);
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
