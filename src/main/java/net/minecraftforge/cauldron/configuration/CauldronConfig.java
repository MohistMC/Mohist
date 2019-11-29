package net.minecraftforge.cauldron.configuration;

import java.lang.reflect.Field;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.cauldron.command.CauldronCommand;
import org.apache.commons.lang3.reflect.FieldUtils;
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
    public final BoolSetting removeErroringBlocks=new BoolSetting(this,"setting.remove-erroring-blocks",true,"Set this to true to remove any Blocks that throws an error in its update method instead of closing the server and reporting a crash log.");

    // Debug settings
    public final BoolSetting enableThreadContentionMonitoring = new BoolSetting(this, "debug.thread-contention-monitoring", false, "Set true to enable Java's thread contention monitoring for thread dumps");

    // Server options
    public final BoolSetting infiniteWaterSource = new BoolSetting(this, "world-settings.default.infinite-water-source", true, "Vanilla water source behavior - is infinite");
    public final BoolSetting flowingLavaDecay = new BoolSetting(this, "world-settings.default.flowing-lava-decay", false, "Lava behaves like vanilla water when source block is removed");
    public final BoolSetting allowTntPunishment = new BoolSetting(this, "world-settings.default.allow-tnt-punishment", true, "TNT ability to push other entities (including other TNTs)");
    public final BoolSetting fakePlayerLogin = new BoolSetting(this, "fake-players.do-login", false, "Raise login events for fake players");
    public final IntSetting maxPlayersVisible = new IntSetting(this, "world-settings.max-players-visible", -1, "How many players will visible in the tab list");

    // Plug-in options
    public final BoolSetting remapPluginFile = new BoolSetting(this, "plugin-settings.default.remap-plugin-file", false, "Remap the plugin file (dev)");

    // Block Monitor
    public final BoolSetting modPacketPlace = new BoolSetting(this, "block-monitor.mod-packet-place", true, "monitor block place on mod packet");
    public final BoolSetting modPacketInteract = new BoolSetting(this, "block-monitor.mod-packet-interact", false, "monitor block interact on mod packet");
    
    //player chunk load 
    public final IntSetting playerChunkLoadDelay = new IntSetting(this, "player-chunk.load-delay", 0, "delay(tick) to load player chunk.");

    /* ======================================================================== */

    public CauldronConfig(String fileName, String commandName)
    {
        super(fileName, commandName);
        init();
    }

    public void init()
    {
        // Uranium start
        for(Field sField : CauldronConfig.class.getDeclaredFields()){
            if(!Setting.class.isAssignableFrom(sField.getType())||sField.getAnnotation(Deprecated.class)!=null)
                continue;

            sField.setAccessible(true);
            try {
                Setting tValue = (Setting) FieldUtils.readField(sField, this);
                if(tValue!=null)
                    register(tValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //Uranium end
        load();
    }
    private void disableLegacyRemap(String pluginName){
        config.addDefault("plugin-settings."+pluginName+".remap-nms-v1_7_R4", true);
        config.addDefault("plugin-settings."+pluginName+".remap-nms-v1_7_R3", false);
        config.addDefault("plugin-settings."+pluginName+".remap-nms-v1_7_R1", false);
        config.addDefault("plugin-settings."+pluginName+".remap-nms-v1_6_R3", false);
        config.addDefault("plugin-settings."+pluginName+".remap-nms-v1_5_R3", false);
        config.addDefault("plugin-settings."+pluginName+".remap-nms-pre", "false");
        config.addDefault("plugin-settings."+pluginName+".remap-obc-v1_7_R4", true);
        config.addDefault("plugin-settings."+pluginName+".remap-obc-v1_7_R3", false);
        config.addDefault("plugin-settings."+pluginName+".remap-obc-v1_7_R1", false);
        config.addDefault("plugin-settings."+pluginName+".remap-obc-v1_6_R3", false);
        config.addDefault("plugin-settings."+pluginName+".remap-obc-v1_5_R3", false);
        config.addDefault("plugin-settings."+pluginName+".remap-obc-pre", false);
    }
    //Uranium start
    public void addDefaults(){
        disableLegacyRemap("WorldEdit");
    }

    private void register(Setting<?> setting) {
        settings.put(setting.path, setting);
    }
    //Uranium end
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
            addDefaults();
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
