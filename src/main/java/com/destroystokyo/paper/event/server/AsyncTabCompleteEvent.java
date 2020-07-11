/*
 * Copyright (c) 2017 Daniel Ennis (Aikar) MIT License
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

package com.destroystokyo.paper.event.server;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows plugins to compute tab completion results asynchronously. If this event provides completions, then the standard synchronous process will not be fired to populate the results. However, the synchronous TabCompleteEvent will fire with the Async results.
 * <p>
 * Only 1 process will be allowed to provide completions, the Async Event, or the standard process.
 */
public class AsyncTabCompleteEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final CommandSender sender;
    private final String buffer;
    private final boolean isCommand;
    private final Location loc;
    private final boolean fireSyncHandler = true;
    private List<String> completions;
    private boolean cancelled;
    private boolean handled = false;

    public AsyncTabCompleteEvent(CommandSender sender, List<String> completions, String buffer, boolean isCommand, Location loc) {
        super(true);
        this.sender = sender;
        this.completions = completions;
        this.buffer = buffer;
        this.isCommand = isCommand;
        this.loc = loc;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the sender completing this command.
     *
     * @return the {@link CommandSender} instance
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * The list of completions which will be offered to the sender, in order.
     * This list is mutable and reflects what will be offered.
     * <p>
     * If this collection is not empty after the event is fired, then
     * the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @return a list of offered completions
     */
    public List<String> getCompletions() {
        return completions;
    }

    /**
     * Set the completions offered, overriding any already set.
     * If this collection is not empty after the event is fired, then
     * the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     * <p>
     * The passed collection will be cloned to a new List. You must call {{@link #getCompletions()}} to mutate from here
     *
     * @param completions the new completions
     */
    public void setCompletions(List<String> completions) {
        Validate.notNull(completions);
        this.completions = new ArrayList<>(completions);
    }

    /**
     * Return the entire buffer which formed the basis of this completion.
     *
     * @return command buffer, as entered
     */
    public String getBuffer() {
        return buffer;
    }

    /**
     * @return True if it is a command being tab completed, false if it is a chat message.
     */
    public boolean isCommand() {
        return isCommand;
    }

    /**
     * @return The position looked at by the sender, or null if none
     */
    public Location getLocation() {
        return loc;
    }

    /**
     * If true, the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @return Is completions considered handled. Always true if completions is not empty.
     */
    public boolean isHandled() {
        return !completions.isEmpty() || handled;
    }

    /**
     * Sets whether or not to consider the completion request handled.
     * If true, the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @param handled if this completion should be marked as being handled
     */
    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Will provide no completions, and will not fire the synchronous process
     *
     * @param cancelled true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
