package com.destroystokyo.paper.event.server;

import com.destroystokyo.paper.exception.ServerException;
import com.google.common.base.Preconditions;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called whenever an exception is thrown in a recoverable section of the server.
 */
public class ServerExceptionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private ServerException exception;

    public ServerExceptionEvent(ServerException exception) {
        this.exception = Preconditions.checkNotNull(exception, "exception");
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the wrapped exception that was thrown.
     *
     * @return Exception thrown
     */
    public ServerException getException() {
        return exception;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
