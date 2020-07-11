package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Represents a plugin loader, which handles direct access to specific types
 * of plugins
 */
public interface PluginLoader {

    /**
     * Loads the plugin contained in the specified file
     *
     * @param file File to attempt to load
     * @return Plugin that was contained in the specified file, or null if
     * unsuccessful
     * @throws InvalidPluginException     Thrown when the specified file is not a
     *                                    plugin
     * @throws UnknownDependencyException If a required dependency could not
     *                                    be found
     */
    Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException;

    /**
     * Loads a PluginDescriptionFile from the specified file
     *
     * @param file File to attempt to load from
     * @return A new PluginDescriptionFile loaded from the plugin.yml in the
     * specified file
     * @throws InvalidDescriptionException If the plugin description file
     *                                     could not be created
     */
    PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException;

    /**
     * Returns a list of all filename filters expected by this PluginLoader
     *
     * @return The filters
     */
    Pattern[] getPluginFileFilters();

    /**
     * Creates and returns registered listeners for the event classes used in
     * this listener
     *
     * @param listener The object that will handle the eventual call back
     * @param plugin   The plugin to use when creating registered listeners
     * @return The registered listeners.
     */
    Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin);

    /**
     * Enables the specified plugin
     * <p>
     * Attempting to enable a plugin that is already enabled will have no
     * effect
     *
     * @param plugin Plugin to enable
     */
    void enablePlugin(Plugin plugin);

    /**
     * Disables the specified plugin
     * <p>
     * Attempting to disable a plugin that is not enabled will have no effect
     *
     * @param plugin Plugin to disable
     */
    void disablePlugin(Plugin plugin);

    // Paper start - close Classloader on disable

    /**
     * Disables the specified plugin
     * <p>
     * Attempting to disable a plugin that is not enabled will have no effect
     *
     * @param plugin           Plugin to disable
     * @param closeClassloader if the classloader for the Plugin should be closed
     */
    // provide default to allow other PluginLoader implementations to work
    default void disablePlugin(Plugin plugin, boolean closeClassloader) {
        disablePlugin(plugin);
    }
    // Paper end - close Classloader on disable
}
