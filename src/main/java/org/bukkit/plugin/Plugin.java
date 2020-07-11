package org.bukkit.plugin;

import org.bukkit.Server;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Represents a Plugin
 * <p>
 * The use of {@link PluginBase} is recommended for actual Implementation
 */
public interface Plugin extends TabExecutor {
    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder
     */
    File getDataFolder();

    /**
     * Returns the plugin.yaml file containing the details for this plugin
     *
     * @return Contents of the plugin.yaml file
     */
    PluginDescriptionFile getDescription();

    /**
     * Gets a {@link FileConfiguration} for this plugin, read through
     * "config.yml"
     * <p>
     * If there is a default config.yml embedded in this plugin, it will be
     * provided as a default for this Configuration.
     *
     * @return Plugin configuration
     */
    FileConfiguration getConfig();

    /**
     * Gets an embedded resource in this plugin
     *
     * @param filename Filename of the resource
     * @return File if found, otherwise null
     */
    InputStream getResource(String filename);

    /**
     * Saves the {@link FileConfiguration} retrievable by {@link #getConfig()}.
     */
    void saveConfig();

    /**
     * Saves the raw contents of the default config.yml file to the location
     * retrievable by {@link #getConfig()}.
     * <p>
     * This should fail silently if the config.yml already exists.
     */
    void saveDefaultConfig();

    /**
     * Saves the raw contents of any resource embedded with a plugin's .jar
     * file assuming it can be found using {@link #getResource(String)}.
     * <p>
     * The resource is saved into the plugin's data folder using the same
     * hierarchy as the .jar file (subdirectories are preserved).
     *
     * @param resourcePath the embedded resource path to look for within the
     *                     plugin's .jar file. (No preceding slash).
     * @param replace      if true, the embedded resource will overwrite the
     *                     contents of an existing file.
     * @throws IllegalArgumentException if the resource path is null, empty,
     *                                  or points to a nonexistent resource.
     */
    void saveResource(String resourcePath, boolean replace);

    /**
     * Discards any data in {@link #getConfig()} and reloads from disk.
     */
    void reloadConfig();

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    PluginLoader getPluginLoader();

    /**
     * Returns the Server instance currently running this plugin
     *
     * @return Server running this plugin
     */
    Server getServer();

    /**
     * Returns a value indicating whether or not this plugin is currently
     * enabled
     *
     * @return true if this plugin is enabled, otherwise false
     */
    boolean isEnabled();

    /**
     * Called when this plugin is disabled
     */
    void onDisable();

    /**
     * Called after a plugin is loaded but before it has been enabled.
     * <p>
     * When multiple plugins are loaded, the onLoad() for all plugins is
     * called before any onEnable() is called.
     */
    void onLoad();

    /**
     * Called when this plugin is enabled
     */
    void onEnable();

    /**
     * Simple boolean if we can still nag to the logs about things
     *
     * @return boolean whether we can nag
     */
    boolean isNaggable();

    /**
     * Set naggable state
     *
     * @param canNag is this plugin still naggable?
     */
    void setNaggable(boolean canNag);

    /**
     * Gets a {@link ChunkGenerator} for use in a default world, as specified
     * in the server configuration
     *
     * @param worldName Name of the world that this will be applied to
     * @param id        Unique ID, if any, that was specified to indicate which
     *                  generator was requested
     * @return ChunkGenerator for use in the default world generation
     */
    ChunkGenerator getDefaultWorldGenerator(String worldName, String id);

    /**
     * Returns the plugin logger associated with this server's logger. The
     * returned logger automatically tags all log messages with the plugin's
     * name.
     *
     * @return Logger associated with this plugin
     */
    Logger getLogger();

    /**
     * Returns the name of the plugin.
     * <p>
     * This should return the bare name of the plugin and should be used for
     * comparison.
     *
     * @return name of the plugin
     */
    String getName();
}
