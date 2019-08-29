package com.destroystokyo.paper.exception;

import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import org.bukkit.Bukkit;

/**
 * Thrown when the internal server throws a recoverable exception.
 */
public class ServerInternalException extends ServerException {

    public ServerInternalException(String message) {
        super(message);
    }

    public ServerInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerInternalException(Throwable cause) {
        super(cause);
    }

    protected ServerInternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void reportInternalException(Throwable cause) {
        try {
            Bukkit.getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(cause)));
        } catch (Throwable t) {
            t.printStackTrace(); // Don't want to rethrow!
        }
    }
}
