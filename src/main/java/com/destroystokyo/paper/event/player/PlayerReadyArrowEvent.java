/*
 * Copyright (c) 2018 Daniel Ennis (Aikar) MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player is firing a bow and the server is choosing an arrow to use.
 */
public class PlayerReadyArrowEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack bow;
    private final ItemStack arrow;
    private boolean cancelled = false;

    public PlayerReadyArrowEvent(Player player, ItemStack bow, ItemStack arrow) {
        super(player);
        this.bow = bow;
        this.arrow = arrow;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return the player is using to fire the arrow
     */
    public ItemStack getBow() {
        return bow;
    }

    /**
     * @return the arrow that is attempting to be used
     */
    public ItemStack getArrow() {
        return arrow;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Whether or not use of this arrow is cancelled. On cancel, the server will try the next arrow available and fire another event.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Cancel use of this arrow. On cancel, the server will try the next arrow available and fire another event.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
