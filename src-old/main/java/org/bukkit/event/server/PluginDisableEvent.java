package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Called when a plugin is disabled.
 */
public class PluginDisableEvent extends PluginEvent {
    private static final HandlerList handlers = new HandlerList();

    public PluginDisableEvent(final Plugin plugin) {
        super(plugin);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
