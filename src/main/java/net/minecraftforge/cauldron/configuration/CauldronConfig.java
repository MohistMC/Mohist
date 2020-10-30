package net.minecraftforge.cauldron.configuration;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.cauldron.command.CauldronCommand;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.file.YamlConfiguration;

public class CauldronConfig extends ConfigBase
{
    private final String HEADER = "This is the main configuration file for Cauldron.\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to Cauldron,\n"
            + "join us at the IRC or drop by our forums and leave a post.\n"
            + "\n"
            + "IRC: #cauldron @ irc.esper.net ( http://webchat.esper.net/?channel=cauldron )\n"
            + "Forums: http://cauldron.minecraftforge.net/\n";
    public static CauldronConfig instance;
    /* ======================================================================== */

    // Logging options
    public final BoolSetting dumpMaterials = new BoolSetting(this, "settings.dump-materials", false, "Dumps all materials with their corresponding id's");
    public final BoolSetting disableWarnings = new BoolSetting(this, "logging.disabled-warnings", false, "Disable warning messages to server admins");
    public final BoolSetting worldLeakDebug = new BoolSetting(this, "logging.world-leak-debug", false, "Log worlds that appear to be leaking (buggy)");
    public final BoolSetting connectionLogging = new BoolSetting(this, "logging.connection", false, "Log connections");
    public final BoolSetting tickIntervalLogging = new BoolSetting(this, "logging.tick-intervals", false, "Log when skip interval handlers are ticked");
    public final BoolSetting chunkLoadLogging = new BoolSetting(this, "logging.chunk-load", false, "Log when chunks are loaded (dev)");
    public final BoolSetting chunkUnloadLogging = new BoolSetting(this, "logging.chunk-unload", false, "Log when chunks are unloaded (dev)");
    public final BoolSetting entitySpawnLogging = new BoolSetting(this, "logging.entity-spawn", false, "Log when living entities are spawned (dev)");
    public final BoolSetting entityDespawnLogging = new BoolSetting(this, "logging.entity-despawn", false, "Log when living entities are despawned (dev)");
    public final BoolSetting entityDeathLogging = new BoolSetting(this, "logging.entity-death", false, "Log when an entity is destroyed (dev)");
    public final BoolSetting logWithStackTraces = new BoolSetting(this, "logging.detailed-logging", false, "Add stack traces to dev logging");
    public final BoolSetting dumpChunksOnDeadlock = new BoolSetting(this, "logging.dump-chunks-on-deadlock", false, "Dump chunks in the event of a deadlock (helps to debug the deadlock)");
    public final BoolSetting dumpHeapOnDeadlock = new BoolSetting(this, "logging.dump-heap-on-deadlock", false, "Dump the heap in the event of a deadlock (helps to debug the deadlock)");
    public final BoolSetting dumpThreadsOnWarn = new BoolSetting(this, "logging.dump-threads-on-warn", false, "Dump the the server thread on deadlock warning (delps to debug the deadlock)");
    public final BoolSetting logEntityCollisionChecks = new BoolSetting(this, "logging.entity-collision-checks", false, "Whether to log entity collision/count checks");
    public final BoolSetting logEntitySpeedRemoval = new BoolSetting(this, "logging.entity-speed-removal", false, "Whether to log entity removals due to speed");
    public final IntSetting largeCollisionLogSize = new IntSetting(this, "logging.collision-warn-size", 200, "Number of colliding entities in one spot before logging a warning. Set to 0 to disable");
    public final IntSetting largeEntityCountLogSize = new IntSetting(this, "logging.entity-count-warn-size", 0, "Number of entities in one dimension logging a warning. Set to 0 to disable");
    public final BoolSetting userLogin = new BoolSetting(this, "logging.user-login", false, "Set true to enable debuggin user's login process");

    // General settings
    public final BoolSetting loadChunkOnRequest = new BoolSetting(this, "settings.load-chunk-on-request", true, "Forces Chunk Loading on 'Provide' requests (speedup for mods that don't check if a chunk is loaded");
    public final BoolSetting loadChunkOnForgeTick = new BoolSetting(this, "settings.load-chunk-on-forge-tick", false, "Forces Chunk Loading during Forge Server Tick events");
    public final BoolSetting checkEntityBoundingBoxes = new BoolSetting(this, "settings.check-entity-bounding-boxes", true, "Removes a living entity that exceeds the max bounding box size.");
    public final BoolSetting checkEntityMaxSpeeds = new BoolSetting(this, "settings.check-entity-max-speeds", false, "Removes any entity that exceeds max speed.");
    public final IntSetting largeBoundingBoxLogSize = new IntSetting(this, "settings.entity-bounding-box-max-size", 1000, "Max size of an entity's bounding box before removing it (either being too large or bugged and 'moving' too fast)");
    public final IntSetting entityMaxSpeed = new IntSetting(this, "settings.entity-max-speed", 100, "Square of the max speed of an entity before removing it");
    public final IntSetting chunkGCGracePeriod = new IntSetting(this, "settings.chunk-gc-grace-period",0,"Grace period of no-ticks before unload");

    // Debug settings
    public final BoolSetting enableThreadContentionMonitoring = new BoolSetting(this, "debug.thread-contention-monitoring", false, "Set true to enable Java's thread contention monitoring for thread dumps");

    // Server options
    public final BoolSetting infiniteWaterSource = new BoolSetting(this, "world-settings.default.infinite-water-source", true, "Vanilla water source behavior - is infinite");
    public final BoolSetting flowingLavaDecay = new BoolSetting(this, "world-settings.default.flowing-lava-decay", false, "Lava behaves like vanilla water when source block is removed");
    public final BoolSetting allowTntPunishment = new BoolSetting(this, "world-settings.default.allow-tnt-punishment", true, "TNT ability to push other entities (including other TNTs)");
    public final BoolSetting fakePlayerLogin = new BoolSetting(this, "fake-players.do-login", false, "Raise login events for fake players");
    public final IntSetting maxPlayersVisible = new IntSetting(this, "world-settings.max-players-visible", -1, "How many players will visible in the tab list");

    // Thermos caterings
    public final BoolSetting realNames = new BoolSetting(this, "world-settings.use-real-names", false, "Instead of DIM##, use the world name prescribed by the mod! Be careful with this one, could create incompat with existing setups!");

    // Optimization options
    public final IntSetting repeaterL = new IntSetting(this, "optimized.redstone-repeater-update-speed", -1, "how many milliseconds the server must ignore before trying repeater updates");
    public final IntSetting redstoneTorchL = new IntSetting(this, "optimized.redstone-redstoneTorch-update-speed", -1, "how many milliseconds the server must ignore before trying redstoneTorch updates");
    public final BoolSetting affinity = new BoolSetting(this, "optimized.affinity-locking", false, "Whether to enable affinity locking. Very technical usage, recommended for dedicated hosts only. Ask on Discord or GitHub for info on how to set this up properly.");
    public final BoolSetting ramChunks = new BoolSetting(this, "optimized.ram-load-chunks", false, "Loads chunks into the system RAM (experimental). WARNING! ENABLING THIS WILL INCREASE RAM USAGE BY OVER 1GB.");
    
    //Protection options
    public final BoolSetting protectSP = new BoolSetting(this, "protection.spawn-protect", true, "Whether to enable Thermos' all-seeing protection in the spawn world");
    public final IntArraySetting instantRemove = new IntArraySetting(this, "protection.instant-removal", "", "Contains Block IDs that you want to NEVER exist in the world i.e. world anchors (just in case) (e.g. instant-removal: 1,93,56,24 ");
    public final StringArraySetting blockedCMDs = new StringArraySetting(this, "protection.blocked-cmds", "", "Contains commands you want to block from being used in-game, you must also include command aliases (e.g. blocked-cmds: /op,/deop,/stop,/restart .");
    public final BoolSetting noFallbackAlias = new BoolSetting(this, "protection.no-fallback-alias", true, "Don't allow commands of the format plugin:cmd, the plugin: will be removed (recommended to keep at true)");
    // Plug-in options
    public final BoolSetting reloadPlugins = new BoolSetting(this, "plugin-settings.allow-reload", false, "Allow plugins to be reloaded. WARNING - breaks with some mods. We *will not* support this!");

    public final BoolSetting remapPluginFile = new BoolSetting(this, "plugin-settings.default.remap-plugin-file", false, "Remap the plugin file (dev)");

    /* ======================================================================== */

    public CauldronConfig(String fileName, String commandName)
    {
        super(fileName, commandName);
        init();
        instance = this;
    }

    public void init()
    {
    	for(Field f : this.getClass().getFields())
    	{
    		if(Modifier.isFinal(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()))
    		{
    			try
    			{
    				Setting setting = (Setting) f.get(this);
    				if(setting == null) continue;
        			settings.put(setting.path, setting);    				
    			}
    			catch (ClassCastException e) 
    			{
    				
    			}
    			catch(Throwable t)
    			{
    				System.out.println("[Thermos] Failed to initialize a CauldronConfig setting.");
    				t.printStackTrace();
    			}

    		}
    	}
        load();
    }

    public void addCommands()
    {
        commands.put(this.commandName, new CauldronCommand());
    }

    public void load()
    {
        try
        {
            config = YamlConfiguration.loadConfiguration(configFile);
            String header = HEADER + "\n";
            for (Setting toggle : settings.values())
            {
                if (!toggle.description.equals(""))
                    header += "Setting: " + toggle.path + " Default: " + toggle.def + "   # " + toggle.description + "\n";

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(config.getString(toggle.path));
            }
            config.options().header(header);
            config.options().copyDefaults(true);

            version = getInt("config-version", 1);
            set("config-version", 1);

            this.saveWorldConfigs();
            this.save();
        }
        catch (Exception ex)
        {
            MinecraftServer.getServer().logSevere("Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}
