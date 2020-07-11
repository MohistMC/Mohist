package org.bukkit;

import com.google.common.collect.ImmutableList;
import org.bukkit.Warning.WarningState;
import org.bukkit.advancement.Advancement;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * Represents a server implementation.
 */
public interface Server extends PluginMessageRecipient {

    /**
     * Used for all administrative messages, such as an operator using a
     * command.
     * <p>
     * For use in {@link #broadcast(java.lang.String, java.lang.String)}.
     */
    String BROADCAST_CHANNEL_ADMINISTRATIVE = "bukkit.broadcast.admin";

    /**
     * Used for all announcement messages, such as informing users that a
     * player has joined.
     * <p>
     * For use in {@link #broadcast(java.lang.String, java.lang.String)}.
     */
    String BROADCAST_CHANNEL_USERS = "bukkit.broadcast.user";

    /**
     * Gets the name of this server implementation.
     *
     * @return name of this server implementation
     */
    String getName();

    /**
     * Gets the version string of this server implementation.
     *
     * @return version of this server implementation
     */
    String getVersion();

    /**
     * Gets the Bukkit version that this server is running.
     *
     * @return version of Bukkit
     */
    String getBukkitVersion();

    Player[] _INVALID_getOnlinePlayers();

    /**
     * Gets a view of all currently logged in players. This {@linkplain
     * Collections#unmodifiableCollection(Collection) view} is a reused
     * object, making some operations like {@link Collection#size()}
     * zero-allocation.
     * <p>
     * The collection is a view backed by the internal representation, such
     * that, changes to the internal state of the server will be reflected
     * immediately. However, the reuse of the returned collection (identity)
     * is not strictly guaranteed for future or all implementations. Casting
     * the collection, or relying on interface implementations (like {@link
     * Serializable} or {@link List}), is deprecated.
     * <p>
     * Iteration behavior is undefined outside of self-contained main-thread
     * uses. Normal and immediate iterator use without consequences that
     * affect the collection are fully supported. The effects following
     * (non-exhaustive) {@link Entity#teleport(Location) teleportation},
     * {@link Player#setHealth(double) death}, and {@link Player#kickPlayer(
     *String) kicking} are undefined. Any use of this collection from
     * asynchronous threads is unsafe.
     * <p>
     * For safe consequential iteration or mimicking the old array behavior,
     * using {@link Collection#toArray(Object[])} is recommended. For making
     * snapshots, {@link ImmutableList#copyOf(Collection)} is recommended.
     *
     * @return a view of currently online players.
     */
    Collection<? extends Player> getOnlinePlayers();

    /**
     * Get the maximum amount of players which can login to this server.
     *
     * @return the amount of players this server allows
     */
    int getMaxPlayers();

    /**
     * Get the game port that the server runs on.
     *
     * @return the port number of this server
     */
    int getPort();

    /**
     * Get the view distance from this server.
     *
     * @return the view distance from this server.
     */
    int getViewDistance();

    /**
     * Get the IP that this server is bound to, or empty string if not
     * specified.
     *
     * @return the IP string that this server is bound to, otherwise empty
     * string
     */
    String getIp();

    /**
     * Get the name of this server.
     *
     * @return the name of this server
     */
    String getServerName();

    /**
     * Get an ID of this server. The ID is a simple generally alphanumeric ID
     * that can be used for uniquely identifying this server.
     *
     * @return the ID of this server
     */
    String getServerId();

    /**
     * Get world type (level-type setting) for default world.
     *
     * @return the value of level-type (e.g. DEFAULT, FLAT, DEFAULT_1_1)
     */
    String getWorldType();

    /**
     * Get generate-structures setting.
     *
     * @return true if structure generation is enabled, false otherwise
     */
    boolean getGenerateStructures();

    /**
     * Gets whether this server allows the End or not.
     *
     * @return whether this server allows the End or not
     */
    boolean getAllowEnd();

    /**
     * Gets whether this server allows the Nether or not.
     *
     * @return whether this server allows the Nether or not
     */
    boolean getAllowNether();

    /**
     * Gets whether this server has a whitelist or not.
     *
     * @return whether this server has a whitelist or not
     */
    boolean hasWhitelist();

    /**
     * Sets if the server is whitelisted.
     *
     * @param value true for whitelist on, false for off
     */
    void setWhitelist(boolean value);

    /**
     * Gets a list of whitelisted players.
     *
     * @return a set containing all whitelisted players
     */
    Set<OfflinePlayer> getWhitelistedPlayers();

    /**
     * Reloads the whitelist from disk.
     */
    void reloadWhitelist();

    /**
     * Broadcast a message to all players.
     * <p>
     * This is the same as calling {@link #broadcast(java.lang.String,
     * java.lang.String)} to {@link #BROADCAST_CHANNEL_USERS}
     *
     * @param message the message
     * @return the number of players
     */
    int broadcastMessage(String message);

    /**
     * Gets the name of the update folder. The update folder is used to safely
     * update plugins at the right moment on a plugin load.
     * <p>
     * The update folder name is relative to the plugins folder.
     *
     * @return the name of the update folder
     */
    String getUpdateFolder();

    /**
     * Gets the update folder. The update folder is used to safely update
     * plugins at the right moment on a plugin load.
     *
     * @return the update folder
     */
    File getUpdateFolderFile();

    /**
     * Gets the value of the connection throttle setting.
     *
     * @return the value of the connection throttle setting
     */
    long getConnectionThrottle();

    /**
     * Gets default ticks per animal spawns value.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn monsters
     *     every tick.
     * <li>A value of 400 will mean the server will attempt to spawn monsters
     *     every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b> If set to 0, animal spawning will be disabled. We
     * recommend using spawn-animals to control this instead.
     * <p>
     * Minecraft default: 400.
     *
     * @return the default ticks per animal spawns value
     */
    int getTicksPerAnimalSpawns();

    /**
     * Gets the default ticks per monster spawns value.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn monsters
     *     every tick.
     * <li>A value of 400 will mean the server will attempt to spawn monsters
     *     every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b> If set to 0, monsters spawning will be disabled. We
     * recommend using spawn-monsters to control this instead.
     * <p>
     * Minecraft default: 1.
     *
     * @return the default ticks per monsters spawn value
     */
    int getTicksPerMonsterSpawns();

    /**
     * Gets a player object by the given username.
     * <p>
     * This method may not return objects for offline players.
     *
     * @param name the name to look up
     * @return a player if one was found, null otherwise
     * @deprecated Use {@link #getPlayer(UUID)} as player names are no longer
     * guaranteed to be unique
     */

    Player getPlayer(String name);

    /**
     * Gets the player with the exact given name, case insensitive.
     *
     * @param name Exact name of the player to retrieve
     * @return a player object if one was found, null otherwise
     * @deprecated Use {@link #getPlayer(UUID)} as player names are no longer
     * guaranteed to be unique
     */

    Player getPlayerExact(String name);

    /**
     * Attempts to match any players with the given name, and returns a list
     * of all possibly matches.
     * <p>
     * This list is not sorted in any particular order. If an exact match is
     * found, the returned list will only contain a single result.
     *
     * @param name the (partial) name to match
     * @return list of all possible players
     * @deprecated Use {@link #getPlayer(UUID)} as player names are no longer
     * guaranteed to be unique
     */

    List<Player> matchPlayer(String name);

    /**
     * Gets the player with the given UUID.
     *
     * @param id UUID of the player to retrieve
     * @return a player object if one was found, null otherwise
     */
    Player getPlayer(UUID id);

    @Nullable
    UUID getPlayerUniqueId(String playerName);

    /**
     * Gets the plugin manager for interfacing with plugins.
     *
     * @return a plugin manager for this Server instance
     */
    PluginManager getPluginManager();

    /**
     * Gets the scheduler for managing scheduled events.
     *
     * @return a scheduling service for this server
     */
    BukkitScheduler getScheduler();

    /**
     * Gets a services manager.
     *
     * @return s services manager
     */
    ServicesManager getServicesManager();

    /**
     * Gets a list of all worlds on this server.
     *
     * @return a list of worlds
     */
    List<World> getWorlds();

    /**
     * Creates or loads a world with the given name using the specified
     * options.
     * <p>
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(creator.name()).
     *
     * @param creator the options to use when creating the world
     * @return newly created or loaded world
     */
    World createWorld(WorldCreator creator);

    /**
     * Unloads a world with the given name.
     *
     * @param name Name of the world to unload
     * @param save whether to save the chunks before unloading
     * @return true if successful, false otherwise
     */
    boolean unloadWorld(String name, boolean save);

    /**
     * Unloads the given world.
     *
     * @param world the world to unload
     * @param save  whether to save the chunks before unloading
     * @return true if successful, false otherwise
     */
    boolean unloadWorld(World world, boolean save);

    /**
     * Gets the world with the given name.
     *
     * @param name the name of the world to retrieve
     * @return a world with the given name, or null if none exists
     */
    World getWorld(String name);

    /**
     * Gets the world from the given Unique ID.
     *
     * @param uid a unique-id of the world to retrieve
     * @return a world with the given Unique ID, or null if none exists
     */
    World getWorld(UUID uid);

    /**
     * Gets the map from the given item ID.
     *
     * @param id the id of the map to get
     * @return a map view if it exists, or null otherwise
     * @deprecated Magic value
     */

    MapView getMap(short id);

    /**
     * Create a new map with an automatically assigned ID.
     *
     * @param world the world the map will belong to
     * @return a newly created map view
     */
    MapView createMap(World world);

    /**
     * Reloads the server, refreshing settings and plugin information.
     */
    void reload();

    /**
     * Reload only the Minecraft data for the server. This includes custom
     * advancements and loot tables.
     */
    void reloadData();

    /**
     * Returns the primary logger associated with this server instance.
     *
     * @return Logger associated with this server
     */
    Logger getLogger();

    /**
     * Gets a {@link PluginCommand} with the given name or alias.
     *
     * @param name the name of the command to retrieve
     * @return a plugin command if found, null otherwise
     */
    PluginCommand getPluginCommand(String name);

    /**
     * Writes loaded players to disk.
     */
    void savePlayers();

    /**
     * Dispatches a command on this server, and executes it if found.
     *
     * @param sender      the apparent sender of the command
     * @param commandLine the command arguments. Example: <code>test abc
     *                    123</code>
     * @return returns false if no target is found
     * @throws CommandException thrown when the executor for the given command
     *                          fails with an unhandled exception
     */
    boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException;

    /**
     * Adds a recipe to the crafting manager.
     *
     * @param recipe the recipe to add
     * @return true if the recipe was added, false if it wasn't for some
     * reason
     */
    boolean addRecipe(Recipe recipe);

    /**
     * Get a list of all recipes for a given item. The stack size is ignored
     * in comparisons. If the durability is -1, it will match any data value.
     *
     * @param result the item to match against recipe results
     * @return a list of recipes with the given result
     */
    List<Recipe> getRecipesFor(ItemStack result);

    /**
     * Get an iterator through the list of crafting recipes.
     *
     * @return an iterator
     */
    Iterator<Recipe> recipeIterator();

    /**
     * Clears the list of crafting recipes.
     */
    void clearRecipes();

    /**
     * Resets the list of crafting recipes to the default.
     */
    void resetRecipes();

    /**
     * Gets a list of command aliases defined in the server properties.
     *
     * @return a map of aliases to command names
     */
    Map<String, String[]> getCommandAliases();

    /**
     * Gets the radius, in blocks, around each worlds spawn point to protect.
     *
     * @return spawn radius, or 0 if none
     */
    int getSpawnRadius();

    /**
     * Sets the radius, in blocks, around each worlds spawn point to protect.
     *
     * @param value new spawn radius, or 0 if none
     */
    void setSpawnRadius(int value);

    /**
     * Gets whether the Server is in online mode or not.
     *
     * @return true if the server authenticates clients, false otherwise
     */
    boolean getOnlineMode();

    /**
     * Gets whether this server allows flying or not.
     *
     * @return true if the server allows flight, false otherwise
     */
    boolean getAllowFlight();

    /**
     * Gets whether the server is in hardcore mode or not.
     *
     * @return true if the server mode is hardcore, false otherwise
     */
    boolean isHardcore();

    /**
     * Shutdowns the server, stopping everything.
     */
    void shutdown();

    /**
     * Broadcasts the specified message to every user with the given
     * permission name.
     *
     * @param message    message to broadcast
     * @param permission the required permission {@link Permissible
     *                   permissibles} must have to receive the broadcast
     * @return number of message recipients
     */
    int broadcast(String message, String permission);

    /**
     * Gets the player by the given name, regardless if they are offline or
     * online.
     * <p>
     * This method may involve a blocking web request to get the UUID for the
     * given name.
     * <p>
     * This will return an object even if the player does not exist. To this
     * method, all players will exist.
     *
     * @param name the name the player to retrieve
     * @return an offline player
     * @see #getOfflinePlayer(java.util.UUID)
     * @deprecated Persistent storage of users should be by UUID as names are no longer
     * unique past a single session.
     */

    OfflinePlayer getOfflinePlayer(String name);

    /**
     * Gets the player by the given UUID, regardless if they are offline or
     * online.
     * <p>
     * This will return an object even if the player does not exist. To this
     * method, all players will exist.
     *
     * @param id the UUID of the player to retrieve
     * @return an offline player
     */
    OfflinePlayer getOfflinePlayer(UUID id);

    /**
     * Gets a set containing all current IPs that are banned.
     *
     * @return a set containing banned IP addresses
     */
    Set<String> getIPBans();

    /**
     * Bans the specified address from the server.
     *
     * @param address the IP address to ban
     */
    void banIP(String address);

    /**
     * Unbans the specified address from the server.
     *
     * @param address the IP address to unban
     */
    void unbanIP(String address);

    /**
     * Gets a set containing all banned players.
     *
     * @return a set containing banned players
     */
    Set<OfflinePlayer> getBannedPlayers();

    /**
     * Gets a ban list for the supplied type.
     * <p>
     * Bans by name are no longer supported and this method will return
     * null when trying to request them. The replacement is bans by UUID.
     *
     * @param type the type of list to fetch, cannot be null
     * @return a ban list of the specified type
     */
    BanList getBanList(BanList.Type type);

    /**
     * Gets a set containing all player operators.
     *
     * @return a set containing player operators
     */
    Set<OfflinePlayer> getOperators();

    /**
     * Gets the default {@link GameMode} for new players.
     *
     * @return the default game mode
     */
    GameMode getDefaultGameMode();

    /**
     * Sets the default {@link GameMode} for new players.
     *
     * @param mode the new game mode
     */
    void setDefaultGameMode(GameMode mode);

    /**
     * Gets a {@link ConsoleCommandSender} that may be used as an input source
     * for this server.
     *
     * @return a console command sender
     */
    ConsoleCommandSender getConsoleSender();

    /**
     * Gets the folder that contains all of the various {@link World}s.
     *
     * @return folder that contains all worlds
     */
    File getWorldContainer();

    /**
     * Gets every player that has ever played on this server.
     *
     * @return an array containing all previous players
     */
    OfflinePlayer[] getOfflinePlayers();

    /**
     * Gets the {@link Messenger} responsible for this server.
     *
     * @return messenger responsible for this server
     */
    Messenger getMessenger();

    /**
     * Gets the {@link HelpMap} providing help topics for this server.
     *
     * @return a help map for this server
     */
    HelpMap getHelpMap();

    /**
     * Creates an empty inventory of the specified type. If the type is {@link
     * InventoryType#CHEST}, the new inventory has a size of 27; otherwise the
     * new inventory has the normal size for its type.
     *
     * @param owner the holder of the inventory, or null to indicate no holder
     * @param type  the type of inventory to create
     * @return a new inventory
     */
    Inventory createInventory(InventoryHolder owner, InventoryType type);

    /**
     * Creates an empty inventory with the specified type and title. If the type
     * is {@link InventoryType#CHEST}, the new inventory has a size of 27;
     * otherwise the new inventory has the normal size for its type.<br>
     * It should be noted that some inventory types do not support titles and
     * may not render with said titles on the Minecraft client.
     *
     * @param owner The holder of the inventory; can be null if there's no holder.
     * @param type  The type of inventory to create.
     * @param title The title of the inventory, to be displayed when it is viewed.
     * @return The new inventory.
     */
    Inventory createInventory(InventoryHolder owner, InventoryType type, String title);

    /**
     * Creates an empty inventory of type {@link InventoryType#CHEST} with the
     * specified size.
     *
     * @param owner the holder of the inventory, or null to indicate no holder
     * @param size  a multiple of 9 as the size of inventory to create
     * @return a new inventory
     * @throws IllegalArgumentException if the size is not a multiple of 9
     */
    Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException;

    /**
     * Creates an empty inventory of type {@link InventoryType#CHEST} with the
     * specified size and title.
     *
     * @param owner the holder of the inventory, or null to indicate no holder
     * @param size  a multiple of 9 as the size of inventory to create
     * @param title the title of the inventory, displayed when inventory is
     *              viewed
     * @return a new inventory
     * @throws IllegalArgumentException if the size is not a multiple of 9
     */
    Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException;

    /**
     * Creates an empty merchant.
     *
     * @param title the title of the corresponding merchant inventory, displayed
     *              when the merchant inventory is viewed
     * @return a new merchant
     */
    Merchant createMerchant(String title);

    /**
     * Gets user-specified limit for number of monsters that can spawn in a
     * chunk.
     *
     * @return the monster spawn limit
     */
    int getMonsterSpawnLimit();

    /**
     * Gets user-specified limit for number of animals that can spawn in a
     * chunk.
     *
     * @return the animal spawn limit
     */
    int getAnimalSpawnLimit();

    /**
     * Gets user-specified limit for number of water animals that can spawn in
     * a chunk.
     *
     * @return the water animal spawn limit
     */
    int getWaterAnimalSpawnLimit();

    /**
     * Gets user-specified limit for number of ambient mobs that can spawn in
     * a chunk.
     *
     * @return the ambient spawn limit
     */
    int getAmbientSpawnLimit();

    /**
     * Checks the current thread against the expected primary thread for the
     * server.
     * <p>
     * <b>Note:</b> this method should not be used to indicate the current
     * synchronized state of the runtime. A current thread matching the main
     * thread indicates that it is synchronized, but a mismatch <b>does not
     * preclude</b> the same assumption.
     *
     * @return true if the current thread matches the expected primary thread,
     * false otherwise
     */
    boolean isPrimaryThread();

    /**
     * Gets the message that is displayed on the server list.
     *
     * @return the servers MOTD
     */
    String getMotd();

    /**
     * Gets the default message that is displayed when the server is stopped.
     *
     * @return the shutdown message
     */
    String getShutdownMessage();

    /**
     * Gets the current warning state for the server.
     *
     * @return the configured warning state
     */
    WarningState getWarningState();

    /**
     * Gets the instance of the item factory (for {@link ItemMeta}).
     *
     * @return the item factory
     * @see ItemFactory
     */
    ItemFactory getItemFactory();

    /**
     * Gets the instance of the scoreboard manager.
     * <p>
     * This will only exist after the first world has loaded.
     *
     * @return the scoreboard manager or null if no worlds are loaded.
     */
    ScoreboardManager getScoreboardManager();

    /**
     * Gets an instance of the server's default server-icon.
     *
     * @return the default server-icon; null values may be used by the
     * implementation to indicate no defined icon, but this behavior is
     * not guaranteed
     */
    CachedServerIcon getServerIcon();

    /**
     * Loads an image from a file, and returns a cached image for the specific
     * server-icon.
     * <p>
     * Size and type are implementation defined. An incompatible file is
     * guaranteed to throw an implementation-defined {@link Exception}.
     *
     * @param file the file to load the from
     * @return a cached server-icon that can be used for a {@link
     * ServerListPingEvent#setServerIcon(CachedServerIcon)}
     * @throws IllegalArgumentException if image is null
     * @throws Exception                if the image does not meet current server server-icon
     *                                  specifications
     */
    CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception;

    /**
     * Creates a cached server-icon for the specific image.
     * <p>
     * Size and type are implementation defined. An incompatible file is
     * guaranteed to throw an implementation-defined {@link Exception}.
     *
     * @param image the image to use
     * @return a cached server-icon that can be used for a {@link
     * ServerListPingEvent#setServerIcon(CachedServerIcon)}
     * @throws IllegalArgumentException if image is null
     * @throws Exception                if the image does not meet current server
     *                                  server-icon specifications
     */
    CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception;

    /**
     * Gets the idle kick timeout.
     *
     * @return the idle timeout in minutes
     */
    int getIdleTimeout();

    /**
     * Set the idle kick timeout. Any players idle for the specified amount of
     * time will be automatically kicked.
     * <p>
     * A value of 0 will disable the idle kick timeout.
     *
     * @param threshold the idle timeout in minutes
     */
    void setIdleTimeout(int threshold);

    /**
     * Create a ChunkData for use in a generator.
     * <p>
     * See {@link ChunkGenerator#generateChunkData(org.bukkit.World, java.util.Random, int, int, org.bukkit.generator.ChunkGenerator.BiomeGrid)}
     *
     * @param world the world to create the ChunkData for
     * @return a new ChunkData for the world
     */
    ChunkGenerator.ChunkData createChunkData(World world);

    /**
     * Creates a boss bar instance to display to players. The progress
     * defaults to 1.0
     *
     * @param title the title of the boss bar
     * @param color the color of the boss bar
     * @param style the style of the boss bar
     * @param flags an optional list of flags to set on the boss bar
     * @return the created boss bar
     */
    BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag... flags);

    /**
     * Gets an entity on the server by its UUID
     *
     * @param uuid the UUID of the entity
     * @return the entity with the given UUID, or null if it isn't found
     */
    Entity getEntity(UUID uuid);

    // Paper start

    /**
     * Gets the current server TPS
     *
     * @return current server TPS (1m, 5m, 15m in Paper-Server)
     */
    double[] getTPS();

    /**
     * Gets the active {@link CommandMap}
     *
     * @return the active command map
     */
    CommandMap getCommandMap();
    // Paper end

    /**
     * Get the advancement specified by this key.
     *
     * @param key unique advancement key
     * @return advancement or null if not exists
     */
    Advancement getAdvancement(NamespacedKey key);

    /**
     * Get an iterator through all advancements. Advancements cannot be removed
     * from this iterator,
     *
     * @return an advancement iterator
     */
    Iterator<Advancement> advancementIterator();

    /**
     * @return the unsafe values instance
     * @see UnsafeValues
     */

    UnsafeValues getUnsafe();

    Spigot spigot();

    /**
     * Checks if player names should be suggested when a command returns {@code null} as
     * their tab completion result.
     *
     * @return true if player names should be suggested
     */
    boolean suggestPlayerNamesWhenNullTabCompletions();
    // Spigot end

    // Paper start - allow preventing player name suggestions by default

    /**
     * Creates a PlayerProfile for the specified uuid, with name as null
     *
     * @param uuid UUID to create profile for
     * @return A PlayerProfile object
     */
    com.destroystokyo.paper.profile.PlayerProfile createProfile(@Nonnull UUID uuid);

    /**
     * Creates a PlayerProfile for the specified name, with UUID as null
     *
     * @param name Name to create profile for
     * @return A PlayerProfile object
     */
    com.destroystokyo.paper.profile.PlayerProfile createProfile(@Nonnull String name);

    /**
     * Creates a PlayerProfile for the specified name/uuid
     * <p>
     * Both UUID and Name can not be null at same time. One must be supplied.
     *
     * @param uuid UUID to create profile for
     * @param name Name to create profile for
     * @return A PlayerProfile object
     */
    com.destroystokyo.paper.profile.PlayerProfile createProfile(@Nullable UUID uuid, @Nullable String name);

    @Nullable
    LootTable getLootTable(@NotNull NamespacedKey key);
    // Paper end

    // Spigot start
    class Spigot {

        @Deprecated
        public org.bukkit.configuration.file.YamlConfiguration getConfig() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public org.bukkit.configuration.file.YamlConfiguration getBukkitConfig() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public org.bukkit.configuration.file.YamlConfiguration getSpigotConfig() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public org.bukkit.configuration.file.YamlConfiguration getPaperConfig() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends the component to the player
         *
         * @param component the components to send
         */
        public void broadcast(net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the player
         *
         * @param components the components to send
         */
        public void broadcast(net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Restart the server. If the server administrator has not configured restarting, the server will stop.
         */
        public void restart() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
