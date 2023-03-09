package org.bukkit.event.block;

import org.bukkit.Warning;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Used when:
 * <ul>
 * <li>A Furnace starts smelting {@link FurnaceStartSmeltEvent}</li>
 * <li>A Brewing-Stand starts brewing {@link BrewingStartEvent}</li>
 * <li>A Campfire starts cooking {@link CampfireStartEvent}</li>
 * </ul>
 *
 * @deprecated draft API
 */
@Deprecated
@Warning(false)
public class InventoryBlockStartEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack source;

    public InventoryBlockStartEvent(@NotNull final Block block, @NotNull ItemStack source) {
        super(block);
        this.source = source;
    }

    /**
     * Gets the source ItemStack for this event.
     *
     * @return the source ItemStack
     */
    @NotNull
    public ItemStack getSource() {
        return source;
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