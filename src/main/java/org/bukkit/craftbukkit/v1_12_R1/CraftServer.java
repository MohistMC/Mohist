package org.bukkit.craftbukkit.v1_12_R1;

import com.destroystokyo.paper.PaperMCConfig;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import jline.console.ConsoleReader;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PendingCommand;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.conversations.Conversable;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.v1_12_R1.boss.CraftBossBar;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftSimpleCommandMap;
import org.bukkit.craftbukkit.v1_12_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.generator.CraftChunkData;
import org.bukkit.craftbukkit.v1_12_R1.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchantCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.RecipeIterator;
import org.bukkit.craftbukkit.v1_12_R1.map.CraftMapView;
import org.bukkit.craftbukkit.v1_12_R1.metadata.EntityMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.v1_12_R1.potion.CraftPotionBrewer;
import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftIconCache;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.util.DatFileFilter;
import org.bukkit.craftbukkit.v1_12_R1.util.Versioning;
import org.bukkit.craftbukkit.v1_12_R1.util.permissions.CraftDefaultPermissions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.command.UnknownCommandEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.Potion;
import org.bukkit.scheduler.BukkitWorker;
import org.bukkit.util.StringUtil;
import org.bukkit.util.permissions.DefaultPermissions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.MarkedYAMLException;
import com.mohistmc.MohistMC;
import com.mohistmc.bukkit.nms.utils.RemapUtils;
import com.mohistmc.configuration.MohistConfig;
import com.mohistmc.forge.MohistForgeUtils;
import com.mohistmc.util.i18n.Message;

public final class CraftServer implements Server {
    static {
        ConfigurationSerialization.registerClass(CraftOfflinePlayer.class);
        CraftItemFactory.instance();
    }

    protected final MinecraftServer console;
    protected final DedicatedPlayerList playerList;
    private final String serverName = MohistMC.NAME;
    private final String serverVersion;
    private final String bukkitVersion = Versioning.getBukkitVersion();
    private final Logger logger = Logger.getLogger("Minecraft");
    private final ServicesManager servicesManager = new SimpleServicesManager();
    private final CraftScheduler scheduler = new CraftScheduler();
    private final CraftSimpleCommandMap craftCommandMap = new CraftSimpleCommandMap(this);
    private final SimpleCommandMap commandMap = new SimpleCommandMap(this);
    public final SimpleHelpMap helpMap = new SimpleHelpMap(this);
    private final StandardMessenger messenger = new StandardMessenger();
    private final SimplePluginManager pluginManager = new SimplePluginManager(this, commandMap);
    private final Map<String, World> worlds = new LinkedHashMap<>();
    private final Yaml yaml = new Yaml(new SafeConstructor());
    private final Map<UUID, OfflinePlayer> offlinePlayers = CacheBuilder.newBuilder().softValues().<UUID, OfflinePlayer>build().asMap();
    private final EntityMetadataStore entityMetadata = new EntityMetadataStore();
    private final PlayerMetadataStore playerMetadata = new PlayerMetadataStore();
    private final WorldMetadataStore worldMetadata = new WorldMetadataStore();
    private final BooleanWrapper online = new BooleanWrapper();
    private final List<CraftPlayer> playerView;
    private final Spigot spigot = new Spigot() {

        @Deprecated
        @Override
        public YamlConfiguration getConfig()
        {
            return org.spigotmc.SpigotConfig.config;
        }

        @Override
        public void restart() {
            org.spigotmc.RestartCommand.restart();
        }

        @Override
        public YamlConfiguration getBukkitConfig()
        {
            return configuration;
        }

        @Override
        public YamlConfiguration getSpigotConfig()
        {
            return org.spigotmc.SpigotConfig.config;
        }

        @Override
        public YamlConfiguration getPaperConfig()
        {
            return PaperMCConfig.config;
        }

        @Override
        public void broadcast(BaseComponent component) {
            for (Player player : getOnlinePlayers()) {
                player.spigot().sendMessage(component);
            }
        }

        @Override
        public void broadcast(BaseComponent... components) {
            for (Player player : getOnlinePlayers()) {
                player.spigot().sendMessage(components);
            }
        }
    };
    public int chunkGCPeriod = -1;
    public int chunkGCLoadThresh = 0;
    public CraftScoreboardManager scoreboardManager;
    public boolean playerCommandState;
    public int reloadCount;
    private YamlConfiguration configuration;
    private YamlConfiguration commandsConfiguration;
    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private int ambientSpawn = -1;
    private File container;
    private WarningState warningState = WarningState.DEFAULT;
    private boolean printSaveWarning;
    private CraftIconCache icon;
    private boolean overrideAllCommandBlockCommands = false;
    private boolean unrestrictedAdvancements;

    public CraftServer(MinecraftServer console, PlayerList playerList) {
        this.console = console;
        this.playerList = (DedicatedPlayerList) playerList;
        this.playerView = Collections.unmodifiableList(Lists.transform(playerList.getPlayers(), EntityPlayerMP::getBukkitEntity));
        this.serverVersion = "git-Mohist-" + MohistMC.getVersion();
        online.value = console.getPropertyManager().getBooleanProperty("online-mode", true);
        Bukkit.setServer(this);
        // Register all the Enchantments and PotionTypes now so we can stop new registration immediately after
        Enchantments.SHARPNESS.getClass();

        Potion.setPotionBrewer(new CraftPotionBrewer());
        MobEffects.BLINDNESS.getClass();
        // Ugly hack :(
        if (!Main.useConsole) {
            getLogger().info("Console input is disabled due to --noconsole command argument");
        }

        configuration = YamlConfiguration.loadConfiguration(getConfigFile());
        configuration.options().copyDefaults(true);
        configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"), Charsets.UTF_8)));
        ConfigurationSection legacyAlias = null;
        if (!configuration.isString("aliases")) {
            legacyAlias = configuration.getConfigurationSection("aliases");
            configuration.set("aliases", "now-in-commands.yml");
        }
        saveConfig();
        if (getCommandsConfigFile().isFile()) {
            legacyAlias = null;
        }
        commandsConfiguration = YamlConfiguration.loadConfiguration(getCommandsConfigFile());
        commandsConfiguration.options().copyDefaults(true);
        commandsConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/commands.yml"), Charsets.UTF_8)));
        saveCommandsConfig();

        // Migrate aliases from old file and add previously implicit $1- to pass all arguments
        if (legacyAlias != null) {
            ConfigurationSection aliases = commandsConfiguration.createSection("aliases");
            for (String key : legacyAlias.getKeys(false)) {
                ArrayList<String> commands = new ArrayList<>();

                if (legacyAlias.isList(key)) {
                    for (String command : legacyAlias.getStringList(key)) {
                        commands.add(command + " $1-");
                    }
                } else {
                    commands.add(legacyAlias.getString(key) + " $1-");
                }

                aliases.set(key, commands);
            }
        }

        saveCommandsConfig();
        overrideAllCommandBlockCommands = commandsConfiguration.getStringList("command-block-overrides").contains("*");
        unrestrictedAdvancements = commandsConfiguration.getBoolean("unrestricted-advancements");
        pluginManager.useTimings(configuration.getBoolean("settings.plugin-profiling"));
        monsterSpawn = configuration.getInt("spawn-limits.monsters");
        animalSpawn = configuration.getInt("spawn-limits.animals");
        waterAnimalSpawn = configuration.getInt("spawn-limits.water-animals");
        ambientSpawn = configuration.getInt("spawn-limits.ambient");
        console.autosavePeriod = configuration.getInt("ticks-per.autosave");
        warningState = WarningState.value(configuration.getString("settings.deprecated-verbose"));
        chunkGCPeriod = Math.min(20,configuration.getInt("chunk-gc.period-in-ticks"));
        chunkGCLoadThresh = configuration.getInt("chunk-gc.load-threshold");
        loadIcon();
    }

    static CraftIconCache loadServerIcon0(File file) throws Exception {
        return loadServerIcon0(ImageIO.read(file));
    }

    static CraftIconCache loadServerIcon0(BufferedImage image) throws Exception {
        ByteBuf bytebuf = Unpooled.buffer();

        Validate.isTrue(image.getWidth() == 64, "Must be 64 pixels wide");
        Validate.isTrue(image.getHeight() == 64, "Must be 64 pixels high");
        ImageIO.write(image, "PNG", new ByteBufOutputStream(bytebuf));
        ByteBuf bytebuf1 = Base64.encode(bytebuf);

        return new CraftIconCache("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8).replace("\n", "")); // Paper - Fix encoding for 1.13+ clients, still compat w/ 1.12 clients
    }

    public boolean getPermissionOverride(ICommandSender listener) {
        while (listener instanceof CommandSenderWrapper) {
            listener = ((CommandSenderWrapper) listener).delegate;
        }
        return unrestrictedAdvancements && listener instanceof AdvancementRewards.AdvancementCommandListener;
    }

    public boolean getCommandBlockOverride(String command) {
        return overrideAllCommandBlockCommands || commandsConfiguration.getStringList("command-block-overrides").contains(command);
    }

    private File getConfigFile() {
        return (File) console.options.valueOf("bukkit-settings");
    }

    private File getCommandsConfigFile() {
        return (File) console.options.valueOf("commands-settings");
    }

    private void saveConfig() {
        try {
            configuration.save(getConfigFile());
        } catch (IOException ex) {
            LogManager.getLogger(CraftServer.class.getName()).error("Could not save " + getConfigFile(), ex);
        }
    }

    private void saveCommandsConfig() {
        try {
            commandsConfiguration.save(getCommandsConfigFile());
        } catch (IOException ex) {
            LogManager.getLogger(CraftServer.class.getName()).error("Could not save " + getCommandsConfigFile(), ex);
        }
    }

    public void loadPlugins() {
        RemapUtils.init();
        pluginManager.registerInterface(JavaPluginLoader.class);

        File pluginFolder = (File) console.options.valueOf("plugins");

        if (pluginFolder.exists()) {
            Plugin[] plugins = pluginManager.loadPlugins(pluginFolder);
            for (Plugin plugin : plugins) {
                try {
                    String message = String.format(Message.getString("mohist.start.plugin_loaded_info"), plugin.getDescription().getFullName());
                    MohistMC.LOGGER.info(message);
                    plugin.onLoad();
                } catch (Throwable ex) {
                    LogManager.getLogger(CraftServer.class.getName()).error(ex.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
                }
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    public void enablePlugins(PluginLoadOrder type) {
        if (type == PluginLoadOrder.STARTUP) {
            helpMap.clear();
            helpMap.initializeGeneralTopics();
            loadCustomPermissions(); // Paper
        }

        Plugin[] plugins = pluginManager.getPlugins();

        for (Plugin plugin : plugins) {
            if ((!plugin.isEnabled()) && (plugin.getDescription().getLoad() == type)) {
                enablePlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            // Spigot start - Allow vanilla commands to be forced to be the main command
            setVanillaCommands(true);
            commandMap.setFallbackCommands();
            this.setVanillaCommands(false);
            // Spigot end
            commandMap.registerServerAliases();
            loadCustomPermissions();
            DefaultPermissions.registerCorePermissions();
            CraftDefaultPermissions.registerCorePermissions();
            helpMap.initializeCommands();
        }

    }

    @Override
    public boolean suggestPlayerNamesWhenNullTabCompletions() {
        return true;
    }

    public void disablePlugins() {
        pluginManager.disablePlugins();
    }

    private void setVanillaCommands(boolean first) {
        Map<String, ICommand> commands = console.getCommandManager().getCommands();
        for (ICommand cmd : commands.values()) {
            // Spigot start
            if (!(cmd instanceof CommandBase)) continue;
            if (console.getCommandManager().getCommandMod().containsValue(cmd)) continue;
            VanillaCommandWrapper wrapper = new VanillaCommandWrapper((CommandBase) cmd, I18n.translateToLocal(cmd.getUsage(null)));
            if (org.spigotmc.SpigotConfig.replaceCommands.contains(wrapper.getName())) {
                if (first) {
                    commandMap.register("minecraft", wrapper);
                }
            } else if (!first) {
                commandMap.register("minecraft", wrapper);
            }
            // Spigot end
        }
    }

    private void enablePlugin(Plugin plugin) {
        try {
            List<Permission> perms = plugin.getDescription().getPermissions();

            for (Permission perm : perms) {
                try {
                    pluginManager.addPermission(perm, false);
                } catch (IllegalArgumentException ex) {
                    MohistMC.LOGGER.warn("Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
                }
            }
            pluginManager.dirtyPermissibles();

            pluginManager.enablePlugin(plugin);
        } catch (Throwable ex) {
            LogManager.getLogger(CraftServer.class.getName()).error(ex.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
    }

    @Override
    public String getName() {
        return serverName;
    }

    @Override
    public String getVersion() {
        return serverVersion + " (MC: " + console.getMinecraftVersion() + ")";
    }

    @Override
    public String getBukkitVersion() {
        return bukkitVersion;
    }

    @Override
    public Player[] _INVALID_getOnlinePlayers() {
        return getOnlinePlayers().toArray(new Player[0]);
    }

    @Override
    public List<CraftPlayer> getOnlinePlayers() {
        return this.playerView;
    }

    @Override
    @Deprecated
    public Player getPlayer(final String name) {
        Validate.notNull(name, "Name cannot be null");

        Player found = getPlayerExact(name);
        // Try for an exact match first.
        if (found != null) {
            return found;
        }

        String lowerName = name.toLowerCase(java.util.Locale.ENGLISH);
        int delta = Integer.MAX_VALUE;
        for (Player player : getOnlinePlayers()) {
            if (player.getName().toLowerCase(java.util.Locale.ENGLISH).startsWith(lowerName)) {
                int curDelta = Math.abs(player.getName().length() - lowerName.length());
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) {
                    break;
                }
            }
        }
        return found;
    }

    @Override
    @Deprecated
    public Player getPlayerExact(String name) {
        Validate.notNull(name, "Name cannot be null");

        EntityPlayerMP player = playerList.getPlayerByUsername(name);
        return (player != null) ? player.getBukkitEntity() : null;
    }

    @Override
    public Player getPlayer(UUID id) {
        EntityPlayerMP player = playerList.getPlayerByUUID(id);

        if (player != null) {
            return player.getBukkitEntity();
        }

        EntityPlayer fakeplayer = net.minecraftforge.common.util.FakePlayerFactory.fakePlayers.get(console.getPlayerProfileCache().getProfileByUUID(id));
        return fakeplayer != null ? (Player)fakeplayer.getBukkitEntity() : null;
    }

    @Override
    public int broadcastMessage(String message) {
        return broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public Player getPlayer(final EntityPlayerMP entity) {
        return entity.getBukkitEntity();
    }

    @Override
    @Deprecated
    public List<Player> matchPlayer(String partialName) {
        Validate.notNull(partialName, "PartialName cannot be null");

        List<Player> matchedPlayers = new ArrayList<>();

        for (Player iterPlayer : this.getOnlinePlayers()) {
            String iterPlayerName = iterPlayer.getName();

            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                // Exact match
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }
            if (iterPlayerName.toLowerCase(java.util.Locale.ENGLISH).contains(partialName.toLowerCase(java.util.Locale.ENGLISH))) {
                // Partial match
                matchedPlayers.add(iterPlayer);
            }
        }

        return matchedPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return playerList.getMaxPlayers();
    }

    // NOTE: These are dependent on the corresponding call in MinecraftServer
    // so if that changes this will need to as well
    @Override
    public int getPort() {
        return this.getConfigInt("server-port", 25565);
    }

    @Override
    public int getViewDistance() {
        return this.getConfigInt("view-distance", 10);
    }

    @Override
    public String getIp() {
        return this.getConfigString("server-ip", "");
    }

    @Override
    public String getServerName() {
        return this.getConfigString("server-name", "Unknown Server");
    }

    @Override
    public String getServerId() {
        return this.getConfigString("server-id", "unnamed");
    }

    @Override
    public String getWorldType() {
        return this.getConfigString("level-type", "DEFAULT");
    }

    @Override
    public boolean getGenerateStructures() {
        return this.getConfigBoolean("generate-structures", true);
    }

    @Override
    public boolean getAllowEnd() {
        return this.configuration.getBoolean("settings.allow-end");
    }

    @Override
    public boolean getAllowNether() {
        return this.getConfigBoolean("allow-nether", true);
    }

    public boolean getWarnOnOverload() {
        return this.configuration.getBoolean("settings.warn-on-overload");
    }

    public boolean getQueryPlugins() {
        return this.configuration.getBoolean("settings.query-plugins");
    }

    @Override
    public boolean hasWhitelist() {
        return this.getConfigBoolean("white-list", false);
    }

    // NOTE: Temporary calls through to server.properies until its replaced
    private String getConfigString(String variable, String defaultValue) {
        return this.console.getPropertyManager().getStringProperty(variable, defaultValue);
    }

    // End Temporary calls

    private int getConfigInt(String variable, int defaultValue) {
        return this.console.getPropertyManager().getIntProperty(variable, defaultValue);
    }

    private boolean getConfigBoolean(String variable, boolean defaultValue) {
        return this.console.getPropertyManager().getBooleanProperty(variable, defaultValue);
    }

    @Override
    public String getUpdateFolder() {
        return this.configuration.getString("settings.update-folder", "update");
    }

    @Override
    public File getUpdateFolderFile() {
        return new File((File) console.options.valueOf("plugins"), this.configuration.getString("settings.update-folder", "update"));
    }

    @Override
    public long getConnectionThrottle() {
        // Spigot Start - Automatically set connection throttle for bungee configurations
        if (org.spigotmc.SpigotConfig.bungee) {
            return -1;
        } else {
            return this.configuration.getInt("settings.connection-throttle");
        }
        // Spigot End
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return this.configuration.getInt("ticks-per.animal-spawns");
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return this.configuration.getInt("ticks-per.monster-spawns");
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public CraftScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public ServicesManager getServicesManager() {
        return servicesManager;
    }

    @Override
    public List<World> getWorlds() {
        return new ArrayList<>(worlds.values());
    }

    public DedicatedPlayerList getHandle() {
        return playerList;
    }

    // NOTE: Should only be called from DedicatedServer.ah()
    public boolean dispatchServerCommand(CommandSender sender, PendingCommand serverCommand) {
        if (sender instanceof Conversable) {
            Conversable conversable = (Conversable) sender;

            if (conversable.isConversing()) {
                conversable.acceptConversationInput(serverCommand.command);
                return true;
            }
        }
        try {
            this.playerCommandState = true;
            return this.dispatchCommand(sender, serverCommand.command);
        } catch (Exception ex) {
            MohistMC.LOGGER.warn("Unexpected exception while parsing console command \"" + serverCommand.command + '"', ex);
            return false;
        } finally {
            this.playerCommandState = false;
        }
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(commandLine, "CommandLine cannot be null");

        if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            craftCommandMap.setVanillaConsoleSender(this.console);
        }

        return this.dispatchVanillaCommand(sender, commandLine);
    }

    // used to process vanilla commands
    public boolean dispatchVanillaCommand(CommandSender sender, String commandLine) {
        if (craftCommandMap.dispatch(sender, commandLine)) {
            return true;
        }
        if (StringUtils.isNotEmpty(MohistConfig.instance.unknownCommandMessage.getValue())) {
            // Paper start
            UnknownCommandEvent event = new UnknownCommandEvent(sender, commandLine, MohistConfig.instance.unknownCommandMessage.getValue());
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (StringUtils.isNotEmpty(event.getMessage())) {
                sender.sendMessage(event.getMessage());
            }
            // Paper end
        }

        return false;
    }

    @Override
    public void reload() {
        reloadCount++;
        configuration = YamlConfiguration.loadConfiguration(getConfigFile());
        commandsConfiguration = YamlConfiguration.loadConfiguration(getCommandsConfigFile());
        PropertyManager config = new PropertyManager(console.options);

        ((DedicatedServer) console).settings = config;

        boolean animals = config.getBooleanProperty("spawn-animals", console.getCanSpawnAnimals());
        boolean monsters = config.getBooleanProperty("spawn-monsters", console.worlds[0].getDifficulty() != EnumDifficulty.PEACEFUL);
        EnumDifficulty difficulty = EnumDifficulty.getDifficultyEnum(config.getIntProperty("difficulty", console.worldServerList.get(0).getDifficulty().ordinal()));

        online.value = config.getBooleanProperty("online-mode", console.isServerInOnlineMode());
        console.setCanSpawnAnimals(config.getBooleanProperty("spawn-animals", console.getCanSpawnAnimals()));
        console.setAllowPvp(config.getBooleanProperty("pvp", console.isPVPEnabled()));
        console.setAllowFlight(config.getBooleanProperty("allow-flight", console.isFlightAllowed()));
        console.setMOTD(config.getStringProperty("motd", console.getMOTD()));
        monsterSpawn = configuration.getInt("spawn-limits.monsters");
        animalSpawn = configuration.getInt("spawn-limits.animals");
        waterAnimalSpawn = configuration.getInt("spawn-limits.water-animals");
        ambientSpawn = configuration.getInt("spawn-limits.ambient");
        warningState = WarningState.value(configuration.getString("settings.deprecated-verbose"));
        printSaveWarning = false;
        console.autosavePeriod = configuration.getInt("ticks-per.autosave");
        chunkGCPeriod = Math.min(20, configuration.getInt("chunk-gc.period-in-ticks"));
        chunkGCLoadThresh = configuration.getInt("chunk-gc.load-threshold");
        loadIcon();

        try {
            playerList.getBannedIPs().readSavedFile();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to load banned-ips.json, " + ex.getMessage());
        }
        try {
            playerList.getBannedPlayers().readSavedFile();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to load banned-players.json, " + ex.getMessage());
        }

        org.spigotmc.SpigotConfig.init((File) console.options.valueOf("spigot-settings")); // Spigot
        PaperMCConfig.init((File) console.options.valueOf("paper-settings"));
        for (WorldServer world : console.worlds) {
            world.worldInfo.setDifficulty(difficulty);
            world.setAllowedSpawnTypes(monsters, animals);
            if (this.getTicksPerAnimalSpawns() < 0) {
                world.ticksPerAnimalSpawns = 400;
            } else {
                world.ticksPerAnimalSpawns = this.getTicksPerAnimalSpawns();
            }

            if (this.getTicksPerMonsterSpawns() < 0) {
                world.ticksPerMonsterSpawns = 1;
            } else {
                world.ticksPerMonsterSpawns = this.getTicksPerMonsterSpawns();
            }
            world.spigotConfig.init(); // Spigot
        }

        pluginManager.clearPlugins();
        commandMap.clearCommands();
        resetRecipes();
        reloadData();
        org.spigotmc.SpigotConfig.registerCommands(); // Spigot
        overrideAllCommandBlockCommands = commandsConfiguration.getStringList("command-block-overrides").contains("*");

        int pollCount = 0;

        // Wait for at most 2.5 seconds for plugins to close their threads
        while (pollCount < 50 && getScheduler().getActiveWorkers().size() > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            pollCount++;
        }

        List<BukkitWorker> overdueWorkers = getScheduler().getActiveWorkers();
        for (BukkitWorker worker : overdueWorkers) {
            Plugin plugin = worker.getOwner();
            String author = "<NoAuthorGiven>";
            if (plugin.getDescription().getAuthors().size() > 0) {
                author = plugin.getDescription().getAuthors().get(0);
            }
            getLogger().log(Level.SEVERE, String.format(
                    "Nag author: '%s' of '%s' about the following: %s",
                    author,
                    plugin.getDescription().getName(),
                    "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin"
            ));
        }
        loadPlugins();
        enablePlugins(PluginLoadOrder.STARTUP);
        enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    @Override
    public void reloadData() {
        console.reload();
    }

    private void loadIcon() {
        icon = new CraftIconCache(null);
        try {
            final File file = new File(new File("."), "server-icon.png");
            if (file.isFile()) {
                icon = loadServerIcon0(file);
            }
        } catch (Exception ex) {
            MohistMC.LOGGER.warn("Couldn't load server icon", ex);
        }
    }

    @SuppressWarnings({"unchecked", "finally"})
    private void loadCustomPermissions() {
        File file = new File(configuration.getString("settings.permissions-file"));
        FileInputStream stream;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            try {
                file.createNewFile();
            } finally {
                return;
            }
        }

        Map<String, Map<String, Object>> perms;

        try {
            perms = (Map<String, Map<String, Object>>) yaml.load(stream);
        } catch (MarkedYAMLException ex) {
            MohistMC.LOGGER.warn("Server permissions file " + file + " is not valid YAML: " + ex.toString());
            return;
        } catch (Throwable ex) {
            MohistMC.LOGGER.warn("Server permissions file " + file + " is not valid YAML.", ex);
            return;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {

            }
        }

        if (perms == null) {
            MohistMC.LOGGER.debug(Message.getFormatString("craftbukkit.craftserver.1", new Object[]{(file)}));

            return;
        }

        List<Permission> permsList = Permission.loadPermissions(perms, "Permission node '%s' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);

        for (Permission perm : permsList) {
            try {
                pluginManager.addPermission(perm);
            } catch (IllegalArgumentException ex) {
                MohistMC.LOGGER.error("Permission in " + file + " was already defined", ex);
            }
        }
    }

    @Override
    public String toString() {
        return "CraftServer{" + "serverName=" + serverName + ",serverVersion=" + serverVersion + ",minecraftVersion=" + console.getMinecraftVersion() + '}';
    }

    public World createWorld(String name, Environment environment) {
        return WorldCreator.name(name).environment(environment).createWorld();
    }

    public World createWorld(String name, Environment environment, long seed) {
        return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
    }

    public World createWorld(String name, Environment environment, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
    }

    public World createWorld(String name, Environment environment, long seed, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
    }

    @Override
    public World createWorld(WorldCreator creator) {
        Validate.notNull(creator, "Creator may not be null");

        String name = creator.name();
        ChunkGenerator generator = creator.generator();
        File folder = new File(getWorldContainer(), name);
        World world = getWorld(name);
        WorldType type = WorldType.parseWorldType(creator.type().getName());
        boolean generateStructures = creator.generateStructures();

        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }

        if (world != null) {
            return world;
        }

        boolean hardcore = false;

        WorldSettings worldSettings = new WorldSettings(creator.seed(), WorldSettings.getGameTypeById(getDefaultGameMode().getValue()), generateStructures, hardcore, type);
        WorldServer internal = DimensionManager.initDimension(creator, worldSettings);

        pluginManager.callEvent(new WorldInitEvent(internal.getWorld()));
        MohistForgeUtils.craftWorldLoading = true;
        System.out.print("Preparing start region for level " + (console.worldServerList.size() - 1) + " (Dimension: " + internal.provider.getDimension() + ", Seed: " + internal.getSeed() + ")"); // Cauldron - log dimension

        if (internal.getWorld().getKeepSpawnInMemory()) {
            short short1 = 196;
            long i = System.currentTimeMillis();
            for (int j = -short1; j <= short1; j += 16) {
                for (int k = -short1; k <= short1; k += 16) {
                    long l = System.currentTimeMillis();

                    if (l < i) {
                        i = l;
                    }

                    if (l > i + 1000L) {
                        int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                        int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;

                        System.out.println("Preparing spawn area for " + name + ", " + (j1 * 100 / i1) + "%");
                        i = l;
                    }

                    BlockPos chunkcoordinates = internal.getSpawnPoint();
                    internal.getChunkProvider().loadChunk(chunkcoordinates.getX() + j >> 4, chunkcoordinates.getZ() + k >> 4);
                }
            }
        }
        pluginManager.callEvent(new WorldLoadEvent(internal.getWorld()));
        MohistForgeUtils.craftWorldLoading = false;
        return internal.getWorld();
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        return unloadWorld(getWorld(name), save);
    }

    @Override
    public boolean unloadWorld(World world, boolean save) {
        if (world == null) {
            return false;
        }

        WorldServer handle = ((CraftWorld) world).getHandle();

        if (!(console.worldServerList.contains(handle))) {
            return false;
        }

        if (handle.dimension == 0) {
            return false;
        }

        if (handle.playerEntities.size() > 0) {
            return false;
        }

        WorldUnloadEvent e = new WorldUnloadEvent(handle.getWorld());
        pluginManager.callEvent(e);

        if (e.isCancelled()) {
            return false;
        }

        if (save) {
            try {
                handle.saveAllChunks(true, null);
                handle.flush();
            } catch (MinecraftException ex) {
                MohistMC.LOGGER.error(ex);
            }
        }
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(handle)); // fire unload event before removing world
        worlds.remove(world.getName().toLowerCase(java.util.Locale.ENGLISH));
        DimensionManager.setWorld(handle.provider.getDimension(), null, FMLCommonHandler.instance().getMinecraftServerInstance()); // remove world from DimensionManager
        return true;
    }

    public MinecraftServer getServer() {
        return console;
    }

    @Override
    public World getWorld(String name) {
        Validate.notNull(name, "Name cannot be null");
        World world = worlds.get(name.toLowerCase(java.util.Locale.ENGLISH));
        if (world == null && name.toUpperCase().startsWith("DIM")) {
            int dimension;
            try {
                dimension = Integer.parseInt(name.substring(3));
                WorldServer worldserver = console.getWorld(dimension);
                if (worldserver != null) {
                    world = worldserver.getWorld();
                }
            } catch (NumberFormatException e) {
            }
        }
        return world;
    }

    @Override
    public World getWorld(UUID uid) {
        for (World world : worlds.values()) {
            if (world.getUID().equals(uid)) {
                return world;
            }
        }
        return null;
    }

    public void addWorld(World world) {
        // Check if a World already exists with the UID.
        if (getWorld(world.getUID()) != null) {
            MohistMC.LOGGER.info(Message.getFormatString("craftserver.addworld", new Object[]{world.getName()}));
            return;
        }
        worlds.put(world.getName().toLowerCase(java.util.Locale.ENGLISH), world);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public ConsoleReader getReader() {
        return this.console.reader;
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        Command command = commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand) command;
        } else {
            return null;
        }
    }

    @Override
    public void savePlayers() {
        checkSaveState();
        playerList.saveAllPlayerData();
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        CraftRecipe toAdd;
        if (recipe instanceof CraftRecipe) {
            toAdd = (CraftRecipe) recipe;
        } else {
            if (recipe instanceof ShapedRecipe) {
                toAdd = CraftShapedRecipe.fromBukkitRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                toAdd = CraftShapelessRecipe.fromBukkitRecipe((ShapelessRecipe) recipe);
            } else if (recipe instanceof FurnaceRecipe) {
                toAdd = CraftFurnaceRecipe.fromBukkitRecipe((FurnaceRecipe) recipe);
            } else {
                return false;
            }
        }
        toAdd.addToCraftingManager();
        return true;
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        Validate.notNull(result, "Result cannot be null");

        List<Recipe> results = new ArrayList<>();
        Iterator<Recipe> iter = recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            ItemStack stack = recipe.getResult();
            if (stack.getType() != result.getType()) {
                continue;
            }
            if (result.getDurability() == -1 || result.getDurability() == stack.getDurability()) {
                results.add(recipe);
            }
        }
        return results;
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return new RecipeIterator();
    }

    @Override
    public void clearRecipes() {
        CraftingManager.REGISTRY = new RegistryNamespaced();
        FurnaceRecipes.instance().smeltingList.clear();
        FurnaceRecipes.instance().customRecipes.clear();
        FurnaceRecipes.instance().customExperience.clear();
    }

    @Override
    public void resetRecipes() {
        CraftingManager.REGISTRY = new RegistryNamespaced();
        CraftingManager.init();
        FurnaceRecipes.instance().smeltingList = new FurnaceRecipes().smeltingList;
        FurnaceRecipes.instance().customRecipes.clear();
        FurnaceRecipes.instance().customExperience.clear();
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        ConfigurationSection section = commandsConfiguration.getConfigurationSection("aliases");
        Map<String, String[]> result = new LinkedHashMap<>();

        if (section != null) {
            for (String key : section.getKeys(false)) {
                List<String> commands;

                if (section.isList(key)) {
                    commands = section.getStringList(key);
                } else {
                    commands = ImmutableList.of(section.getString(key));
                }

                result.put(key, commands.toArray(new String[commands.size()]));
            }
        }

        return result;
    }

    public void removeBukkitSpawnRadius() {
        configuration.set("settings.spawn-radius", null);
        saveConfig();
    }

    public int getBukkitSpawnRadius() {
        return configuration.getInt("settings.spawn-radius", -1);
    }

    @Override
    public String getShutdownMessage() {
        return configuration.getString("settings.shutdown-message");
    }

    @Override
    public int getSpawnRadius() {
        return ((DedicatedServer) console).settings.getIntProperty("spawn-protection", 16);
    }

    @Override
    public void setSpawnRadius(int value) {
        configuration.set("settings.spawn-radius", value);
        saveConfig();
    }

    @Override
    public boolean getOnlineMode() {
        return online.value;
    }

    @Override
    public boolean getAllowFlight() {
        return console.isFlightAllowed();
    }

    @Override
    public boolean isHardcore() {
        return console.isHardcore();
    }

    public ChunkGenerator getGenerator(String world) {
        ConfigurationSection section = configuration.getConfigurationSection("worlds");
        ChunkGenerator result = null;

        if (section != null) {
            section = section.getConfigurationSection(world);

            if (section != null) {
                String name = section.getString("generator");

                if ((name != null) && (!name.equals(""))) {
                    String[] split = name.split(":", 2);
                    String id = (split.length > 1) ? split[1] : null;
                    Plugin plugin = pluginManager.getPlugin(split[0]);

                    if (plugin == null) {
                        MohistMC.LOGGER.error("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
                    } else if (!plugin.isEnabled()) {
                        MohistMC.LOGGER.error("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
                    } else {
                        try {
                            result = plugin.getDefaultWorldGenerator(world, id);
                            if (result == null) {
                                MohistMC.LOGGER.error("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world generator");
                            }
                        } catch (Throwable t) {
                            MohistMC.LOGGER.error("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t);
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    @Deprecated
    public CraftMapView getMap(short id) {
        MapStorage collection = console.worlds[0].mapStorage;
        MapData worldmap = (MapData) collection.getOrLoadData(MapData.class, "map_" + id);
        if (worldmap == null) {
            return null;
        }
        return worldmap.mapView;
    }

    @Override
    public CraftMapView createMap(World world) {
        Validate.notNull(world, "World cannot be null");

        net.minecraft.item.ItemStack stack = new net.minecraft.item.ItemStack(Items.MAP, 1, -1, true);
        MapData worldmap = Items.FILLED_MAP.getMapData(stack, ((CraftWorld) world).getHandle());
        return worldmap.mapView;
    }

    @Override
    public void shutdown() {
        console.initiateShutdown();
    }

    @Override
    public int broadcast(String message, String permission) {
        Set<CommandSender> recipients = new HashSet<>();
        for (Permissible permissible : getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                recipients.add((CommandSender) permissible);
            }
        }

        BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(message, recipients);
        getPluginManager().callEvent(broadcastMessageEvent);

        if (broadcastMessageEvent.isCancelled()) {
            return 0;
        }

        message = broadcastMessageEvent.getMessage();

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    // Paper start
    @Nullable
    public UUID getPlayerUniqueId(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null) {
            return player.getUniqueId();
        }
        GameProfile profile;
        // Only fetch an online UUID in online mode
        if (MinecraftServer.getServerInst().isServerInOnlineMode() || MohistConfig.isProxyOnlineMode()) {
            profile = console.getPlayerProfileCache().getGameProfileForUsername(name);
        } else {
            // Make an OfflinePlayer using an offline mode UUID since the name has no profile
            profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name);
        }
        return profile != null ? profile.getId() : null;
    }

    @Override
    @Deprecated
    public OfflinePlayer getOfflinePlayer(String name) {
        Validate.notNull(name, "Name cannot be null");
        com.google.common.base.Preconditions.checkArgument( !org.apache.commons.lang.StringUtils.isBlank( name ), "Name cannot be blank" ); // Spigot
        OfflinePlayer result = getPlayerExact(name);
        if (result == null) {
            // This is potentially blocking :(
            GameProfile profile = console.getPlayerProfileCache().getGameProfileForUsername(name);
            if (profile == null) {
                // Make an OfflinePlayer using an offline mode UUID since the name has no profile
                result = getOfflinePlayer(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name));
            } else {
                // Use the GameProfile even when we get a UUID so we ensure we still have a name
                result = getOfflinePlayer(profile);
            }
        } else {
            offlinePlayers.remove(result.getUniqueId());
        }

        return result;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        Validate.notNull(id, "UUID cannot be null");

        OfflinePlayer result = getPlayer(id);
        if (result == null) {
            result = offlinePlayers.get(id);
            if (result == null) {
                result = new CraftOfflinePlayer(this, new GameProfile(id, null));
                offlinePlayers.put(id, result);
            }
        } else {
            offlinePlayers.remove(id);
        }

        return result;
    }

    public OfflinePlayer getOfflinePlayer(GameProfile profile) {
        OfflinePlayer player = new CraftOfflinePlayer(this, profile);
        offlinePlayers.put(profile.getId(), player);
        return player;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> getIPBans() {
        return new HashSet<>(Arrays.asList(playerList.getBannedIPs().getKeys()));
    }

    @Override
    public void banIP(String address) {
        Validate.notNull(address, "Address cannot be null.");

        this.getBanList(BanList.Type.IP).addBan(address, null, null, null);
    }

    @Override
    public void unbanIP(String address) {
        Validate.notNull(address, "Address cannot be null.");

        this.getBanList(BanList.Type.IP).pardon(address);
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        Set<OfflinePlayer> result = new HashSet<>();

        for (UserListEntry entry : playerList.getBannedPlayers().getValuesCB()) {
            result.add(getOfflinePlayer((GameProfile) entry.getValue()));
        }

        return result;
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        Validate.notNull(type, "Type cannot be null");

        switch (type) {
            case IP:
                return new CraftIpBanList(playerList.getBannedIPs());
            case NAME:
            default:
                return new CraftProfileBanList(playerList.getBannedPlayers());
        }
    }

    @Override
    public void setWhitelist(boolean value) {
        playerList.setWhiteListEnabled(value);
        console.getPropertyManager().setProperty("white-list", value);
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        Set<OfflinePlayer> result = new LinkedHashSet<>();

        for (UserListEntry entry : playerList.getWhitelistedPlayers().getValuesCB()) {
            result.add(getOfflinePlayer((GameProfile) entry.getValue()));
        }

        return result;
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        Set<OfflinePlayer> result = new HashSet<>();

        for (UserListEntry entry : playerList.getOppedPlayers().getValuesCB()) {
            result.add(getOfflinePlayer((GameProfile) entry.getValue()));
        }

        return result;
    }

    @Override
    public void reloadWhitelist() {
        playerList.reloadWhitelist();
    }

    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(console.worlds[0].getWorldInfo().getGameType().getID());
    }

    @Override
    public void setDefaultGameMode(GameMode mode) {
        Validate.notNull(mode, "Mode cannot be null");

        for (World world : getWorlds()) {
            ((CraftWorld) world).getHandle().worldInfo.setGameType(GameType.getByID(mode.getValue()));
        }
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return console.console;
    }

    public EntityMetadataStore getEntityMetadata() {
        return entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return playerMetadata;
    }

    public WorldMetadataStore getWorldMetadata() {
        return worldMetadata;
    }

    @Override
    public File getWorldContainer() {
        // Cauldron start - return the proper container
        if (DimensionManager.getWorld(0) != null) {
            return ((SaveHandler) DimensionManager.getWorld(0).getSaveHandler()).getWorldDirectory();
        }
        // Cauldron end
        if (this.getServer().anvilFile != null) {
            return this.getServer().anvilFile;
        }

        if (container == null) {
            container = new File(configuration.getString("settings.world-container", "."));
        }

        return container;
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        SaveHandler storage = (SaveHandler) console.worlds[0].getSaveHandler();
        String[] files = storage.getPlayerDir().list(new DatFileFilter());
        Set<OfflinePlayer> players = new HashSet<>();

        for (String file : files) {
            try {
                players.add(getOfflinePlayer(UUID.fromString(file.substring(0, file.length() - 4))));
            } catch (IllegalArgumentException ex) {
                // Who knows what is in this directory, just ignore invalid files
            }
        }

        players.addAll(getOnlinePlayers());

        return players.toArray(new OfflinePlayer[players.size()]);
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(getMessenger(), source, channel, message);

        for (Player player : getOnlinePlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<>();

        for (Player player : getOnlinePlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        // TODO: Create the appropriate type, rather than Custom?
        return new CraftInventoryCustom(owner, type);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return new CraftInventoryCustom(owner, type, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
        return new CraftInventoryCustom(owner, size);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
        return new CraftInventoryCustom(owner, size, title);
    }

    @Override
    public Merchant createMerchant(String title) {
        return new CraftMerchantCustom(title == null ? InventoryType.MERCHANT.getDefaultTitle() : title);
    }

    @Override
    public HelpMap getHelpMap() {
        return helpMap;
    }

    @Override // Paper - add override
    public SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    // Cauldron start
    public CraftSimpleCommandMap getCraftCommandMap() {
        return craftCommandMap;
    }
	// Cauldron end

    @Override
    public int getMonsterSpawnLimit() {
        return monsterSpawn;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return animalSpawn;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return waterAnimalSpawn;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return ambientSpawn;
    }

    @Override
    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(console.primaryThread);
    }

    @Override
    public String getMotd() {
        return console.getMOTD();
    }

    @Override
    public WarningState getWarningState() {
        return warningState;
    }

    public List<String> tabComplete(net.minecraft.command.ICommandSender sender, String message, BlockPos pos, boolean forceCommand) {
        // Spigot Start
        if ((org.spigotmc.SpigotConfig.tabComplete < 0 || message.length() <= org.spigotmc.SpigotConfig.tabComplete) && !message.contains(" ")) {
            return ImmutableList.of();
        }
        // Spigot End
        if (!(sender instanceof EntityPlayerMP)) {
            return ImmutableList.of();
        }

        List<String> offers;
        Player player = ((EntityPlayerMP) sender).getBukkitEntity();
        if (message.startsWith("/") || forceCommand) {
            offers = tabCompleteCommand(player, message, pos);
        } else {
            offers = tabCompleteChat(player, message);
        }

        TabCompleteEvent tabEvent = new TabCompleteEvent(player, message, offers);
        getPluginManager().callEvent(tabEvent);

        return tabEvent.isCancelled() ? Collections.EMPTY_LIST : tabEvent.getCompletions();
    }

    public List<String> tabCompleteCommand(Player player, String message, BlockPos pos) {
        // Spigot Start
        if ( (org.spigotmc.SpigotConfig.tabComplete < 0 || message.length() <= org.spigotmc.SpigotConfig.tabComplete) && !message.contains( " " ) )
        {
            return ImmutableList.of();
        }
        // Spigot End
        List<String> completions = null;
        try {
            if (message.startsWith("/")) {
                // Trim leading '/' if present (won't always be present in command blocks)
                message = message.substring(1);
            }
            if (pos == null) {
                completions = getCommandMap().tabComplete(player, message);
            } else {
                completions = getCommandMap().tabComplete(player, message, new Location(player.getWorld(), pos.getX(), pos.getY(), pos.getZ()));
            }
        } catch (CommandException ex) {
            player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to tab-complete this command");
            MohistMC.LOGGER.error("Exception when " + player.getName() + " attempted to tab complete " + message, ex);
        }

        return completions == null ? ImmutableList.<String>of() : completions;
    }

    public List<String> tabCompleteChat(Player player, String message) {
        List<String> completions = new ArrayList<>();
        PlayerChatTabCompleteEvent event = new PlayerChatTabCompleteEvent(player, message, completions);
        String token = event.getLastToken();
        for (Player p : getOnlinePlayers()) {
            if (player.canSee(p) && StringUtil.startsWithIgnoreCase(p.getName(), token)) {
                completions.add(p.getName());
            }
        }
        pluginManager.callEvent(event);

        // Sanity
        completions.removeIf(current -> !(current instanceof String));
        Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
        return completions;
    }

    @Override
    public CraftItemFactory getItemFactory() {
        return CraftItemFactory.instance();
    }

    @Override
    public CraftScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public void checkSaveState() {
        if (this.playerCommandState || this.printSaveWarning || this.console.autosavePeriod <= 0) {
            return;
        }
        this.printSaveWarning = true;
        MohistMC.LOGGER.warn("A manual (plugin-induced) save has been detected while server is configured to auto-save. This may affect performance.", warningState == WarningState.ON ? new Throwable() : null);
    }

    @Override
    public CraftIconCache getServerIcon() {
        return icon;
    }

    @Override
    public CraftIconCache loadServerIcon(File file) throws Exception {
        Validate.notNull(file, "File cannot be null");
        if (!file.isFile()) {
            throw new IllegalArgumentException(file + " is not a file");
        }
        return loadServerIcon0(file);
    }

    @Override
    public CraftIconCache loadServerIcon(BufferedImage image) throws Exception {
        Validate.notNull(image, "Image cannot be null");
        return loadServerIcon0(image);
    }

    @Override
    public int getIdleTimeout() {
        return console.getMaxPlayerIdleMinutes();
    }

    @Override
    public void setIdleTimeout(int threshold) {
        console.setPlayerIdleTimeout(threshold);
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        return new CraftChunkData(world);
    }

    @Override
    public BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        return new CraftBossBar(title, color, style, flags);
    }

    @Override
    public Entity getEntity(UUID uuid) {
        Validate.notNull(uuid, "UUID cannot be null");
        net.minecraft.entity.Entity entity = console.getEntityFromUuid(uuid); // PAIL: getEntity
        return entity == null ? null : entity.getBukkitEntity();
    }

    @Override
    public org.bukkit.advancement.Advancement getAdvancement(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "key");

        Advancement advancement = console.getAdvancementManager().getAdvancement(CraftNamespacedKey.toMinecraft(key));
        return (advancement == null) ? null : advancement.bukkit;
    }

    @Override
    public Iterator<org.bukkit.advancement.Advancement> advancementIterator() {
        // PAIL: rename
        return Iterators.unmodifiableIterator(Iterators.transform(console.getAdvancementManager().getAdvancements().iterator(), advancement -> advancement.bukkit));
    }

    @Deprecated
    @Override
    public UnsafeValues getUnsafe() {
        return CraftMagicNumbers.INSTANCE;
    }

    // Paper - Add getTPS API - Further improve tick loop
    @Override
    public double[] getTPS() {
        return new double[]{
                MinecraftServer.getServerInst().recentTps[0],
                MinecraftServer.getServerInst().recentTps[1],
                MinecraftServer.getServerInst().recentTps[2],
        };
    }
    // Paper end

    public Spigot spigot() {
        return spigot;
    }

    public com.destroystokyo.paper.profile.PlayerProfile createProfile(@Nonnull UUID uuid) {
        return createProfile(uuid, null);
    }

    public com.destroystokyo.paper.profile.PlayerProfile createProfile(@Nonnull String name) {
        return createProfile(null, name);
    }

    public com.destroystokyo.paper.profile.PlayerProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        Player player = uuid != null ? Bukkit.getPlayer(uuid) : (name != null ? Bukkit.getPlayerExact(name) : null);
        if (player != null) {
            return new com.destroystokyo.paper.profile.CraftPlayerProfile((CraftPlayer) player);
        }
        return new com.destroystokyo.paper.profile.CraftPlayerProfile(uuid, name);
    }

    private static final class BooleanWrapper {
        private boolean value = true;
    }
    // Paper end
}
