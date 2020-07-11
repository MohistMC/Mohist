package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.util.Set;

/**
 * Handles all plugin management from the Server
 */
public interface PluginManager {

    /**
     * Registers the specified plugin loader
     *
     * @param loader Class name of the PluginLoader to register
     * @throws IllegalArgumentException Thrown when the given Class is not a
     *                                  valid PluginLoader
     */
    void registerInterface(Class<? extends PluginLoader> loader) throws IllegalArgumentException;

    /**
     * Checks if the given plugin is loaded and returns it when applicable
     * <p>
     * Please note that the name of the plugin is case-sensitive
     *
     * @param name Name of the plugin to check
     * @return Plugin if it exists, otherwise null
     */
    Plugin getPlugin(String name);

    /**
     * Gets a list of all currently loaded plugins
     *
     * @return Array of Plugins
     */
    Plugin[] getPlugins();

    /**
     * Checks if the given plugin is enabled or not
     * <p>
     * Please note that the name of the plugin is case-sensitive.
     *
     * @param name Name of the plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    boolean isPluginEnabled(String name);

    /**
     * Checks if the given plugin is enabled or not
     *
     * @param plugin Plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    boolean isPluginEnabled(Plugin plugin);

    /**
     * Loads the plugin in the specified file
     * <p>
     * File must be valid according to the current enabled Plugin interfaces
     *
     * @param file File containing the plugin to load
     * @return The Plugin loaded, or null if it was invalid
     * @throws InvalidPluginException      Thrown when the specified file is not a
     *                                     valid plugin
     * @throws InvalidDescriptionException Thrown when the specified file
     *                                     contains an invalid description
     * @throws UnknownDependencyException  If a required dependency could not
     *                                     be resolved
     */
    Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException;

    /**
     * Loads the plugins contained within the specified directory
     *
     * @param directory Directory to check for plugins
     * @return A list of all plugins loaded
     */
    Plugin[] loadPlugins(File directory);

    /**
     * Disables all the loaded plugins
     */
    void disablePlugins();

    /**
     * Disables and removes all plugins
     */
    void clearPlugins();

    /**
     * Calls an event with the given details
     *
     * @param event Event details
     * @throws IllegalStateException Thrown when an asynchronous event is
     *                               fired from synchronous code.
     *                               <p>
     *                               <i>Note: This is best-effort basis, and should not be used to test
     *                               synchronized state. This is an indicator for flawed flow logic.</i>
     */
    void callEvent(Event event) throws IllegalStateException;

    /**
     * Registers all the events in the given listener class
     *
     * @param listener Listener to register
     * @param plugin   Plugin to register
     */
    void registerEvents(Listener listener, Plugin plugin);

    /**
     * Registers the specified executor to the given event class
     *
     * @param event    Event type to register
     * @param listener Listener to register
     * @param priority Priority to register this event at
     * @param executor EventExecutor to register
     * @param plugin   Plugin to register
     */
    void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin);

    /**
     * Registers the specified executor to the given event class
     *
     * @param event           Event type to register
     * @param listener        Listener to register
     * @param priority        Priority to register this event at
     * @param executor        EventExecutor to register
     * @param plugin          Plugin to register
     * @param ignoreCancelled Whether to pass cancelled events or not
     */
    void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin, boolean ignoreCancelled);

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

    /**
     * Gets a {@link Permission} from its fully qualified name
     *
     * @param name Name of the permission
     * @return Permission, or null if none
     */
    Permission getPermission(String name);

    /**
     * Adds a {@link Permission} to this plugin manager.
     * <p>
     * If a permission is already defined with the given name of the new
     * permission, an exception will be thrown.
     *
     * @param perm Permission to add
     * @throws IllegalArgumentException Thrown when a permission with the same
     *                                  name already exists
     */
    void addPermission(Permission perm);

    /**
     * Removes a {@link Permission} registration from this plugin manager.
     * <p>
     * If the specified permission does not exist in this plugin manager,
     * nothing will happen.
     * <p>
     * Removing a permission registration will <b>not</b> remove the
     * permission from any {@link Permissible}s that have it.
     *
     * @param perm Permission to remove
     */
    void removePermission(Permission perm);

    /**
     * Removes a {@link Permission} registration from this plugin manager.
     * <p>
     * If the specified permission does not exist in this plugin manager,
     * nothing will happen.
     * <p>
     * Removing a permission registration will <b>not</b> remove the
     * permission from any {@link Permissible}s that have it.
     *
     * @param name Permission to remove
     */
    void removePermission(String name);

    /**
     * Gets the default permissions for the given op status
     *
     * @param op Which set of default permissions to get
     * @return The default permissions
     */
    Set<Permission> getDefaultPermissions(boolean op);

    /**
     * Recalculates the defaults for the given {@link Permission}.
     * <p>
     * This will have no effect if the specified permission is not registered
     * here.
     *
     * @param perm Permission to recalculate
     */
    void recalculatePermissionDefaults(Permission perm);

    /**
     * Subscribes the given Permissible for information about the requested
     * Permission, by name.
     * <p>
     * If the specified Permission changes in any form, the Permissible will
     * be asked to recalculate.
     *
     * @param permission  Permission to subscribe to
     * @param permissible Permissible subscribing
     */
    void subscribeToPermission(String permission, Permissible permissible);

    /**
     * Unsubscribes the given Permissible for information about the requested
     * Permission, by name.
     *
     * @param permission  Permission to unsubscribe from
     * @param permissible Permissible subscribing
     */
    void unsubscribeFromPermission(String permission, Permissible permissible);

    /**
     * Gets a set containing all subscribed {@link Permissible}s to the given
     * permission, by name
     *
     * @param permission Permission to query for
     * @return Set containing all subscribed permissions
     */
    Set<Permissible> getPermissionSubscriptions(String permission);

    /**
     * Subscribes to the given Default permissions by operator status
     * <p>
     * If the specified defaults change in any form, the Permissible will be
     * asked to recalculate.
     *
     * @param op          Default list to subscribe to
     * @param permissible Permissible subscribing
     */
    void subscribeToDefaultPerms(boolean op, Permissible permissible);

    /**
     * Unsubscribes from the given Default permissions by operator status
     *
     * @param op          Default list to unsubscribe from
     * @param permissible Permissible subscribing
     */
    void unsubscribeFromDefaultPerms(boolean op, Permissible permissible);

    /**
     * Gets a set containing all subscribed {@link Permissible}s to the given
     * default list, by op status
     *
     * @param op Default list to query for
     * @return Set containing all subscribed permissions
     */
    Set<Permissible> getDefaultPermSubscriptions(boolean op);

    /**
     * Gets a set of all registered permissions.
     * <p>
     * This set is a copy and will not be modified live.
     *
     * @return Set containing all current registered permissions
     */
    Set<Permission> getPermissions();

    /**
     * Returns whether or not timing code should be used for event calls
     *
     * @return True if event timings are to be used
     */
    boolean useTimings();
}
