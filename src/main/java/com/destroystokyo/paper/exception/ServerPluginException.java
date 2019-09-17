package com.destroystokyo.paper.exception;

import static com.google.common.base.Preconditions.checkNotNull;
import org.bukkit.plugin.Plugin;

/**
 * Wrapper exception for all cases to which a plugin can be immediately blamed for
 */
public class ServerPluginException extends ServerException {
    private final Plugin responsiblePlugin;

    public ServerPluginException(String message, Throwable cause, Plugin responsiblePlugin) {
        super(message, cause);
        this.responsiblePlugin = checkNotNull(responsiblePlugin, "responsiblePlugin");
    }

    public ServerPluginException(Throwable cause, Plugin responsiblePlugin) {
        super(cause);
        this.responsiblePlugin = checkNotNull(responsiblePlugin, "responsiblePlugin");
    }

    protected ServerPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Plugin responsiblePlugin) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responsiblePlugin = checkNotNull(responsiblePlugin, "responsiblePlugin");
    }

    /**
     * Gets the plugin which is directly responsible for the exception being thrown
     *
     * @return plugin which is responsible for the exception throw
     */
    public Plugin getResponsiblePlugin() {
        return responsiblePlugin;
    }
}
