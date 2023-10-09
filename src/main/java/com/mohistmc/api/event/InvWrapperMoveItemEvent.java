package com.mohistmc.api.event;

import com.google.common.base.Preconditions;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/10 3:29:10
 */
public class InvWrapperMoveItemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final Inventory inventory;
    private final ItemStack itemStack;

    public InvWrapperMoveItemEvent(@NotNull final Inventory inventory, @NotNull final ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "ItemStack cannot be null");
        this.inventory = inventory;
        this.itemStack = itemStack;
    }

    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    @NotNull
    public ItemStack getItem() {
        return itemStack.clone();
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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


    public static class Extract extends InvWrapperMoveItemEvent implements Cancellable {

        private static final HandlerList handlers = new HandlerList();
        private boolean cancelled;

        public Extract(@NotNull Inventory inventory, @NotNull ItemStack itemStack) {
            super(inventory, itemStack);
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            this.cancelled = cancel;
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

    public static class Insert extends InvWrapperMoveItemEvent implements Cancellable {

        private static final HandlerList handlers = new HandlerList();
        private boolean cancelled;

        public Insert(@NotNull Inventory inventory, @NotNull ItemStack itemStack) {
            super(inventory, itemStack);
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            this.cancelled = cancel;
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
}
