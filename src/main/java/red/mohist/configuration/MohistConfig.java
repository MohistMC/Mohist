package red.mohist.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.api.ServerAPI;
import red.mohist.command.DumpCommand;
import red.mohist.command.MohistCommand;
import red.mohist.util.i18n.Message;

public class MohistConfig extends ConfigBase{

    private final String HEADER = "This is the main configuration file for Mohist.\n"
            + "\n"
            + "Home: https://www.mohist.red/\n";

    public static MohistConfig instance;

    /* ======================================================================== */

    public final StringSetting unknownCommandMessage = new StringSetting(this, "messages.use-unknow-command", Message.getString("use.unknow.command"), "Prompt unknown command");
    public final StringSetting outdatedClientMessage = new StringSetting(this, "messages.Outdate-Client", Message.getString("outdate.client"), "Outdate Client");
    public final StringSetting outdatedServerMessage = new StringSetting(this, "messages.Outdate-Server", Message.getString("outdate.server"), "Outdate Server");

    public final StringSetting rejectionsHackMessage = new StringSetting(this, "messages.Rejections-Hack", Message.getString("rejections.hack"), "Hack rejections");
    public final StringSetting rejectionsServerModsMessage = new StringSetting(this, "messages.Rejections-Server-Mods", Message.getString("rejections.server-mods"), "Server-mods rejections");

    public final StringSetting requirementsModInvalidVersion = new StringSetting(this, "messages.Requirements-Mod-Invalid-version", Message.getString("requirements.mod.invalid-version"), "Requires mod version X but client has Y");
    public final StringSetting requirementsModNotFound = new StringSetting(this, "messages.Requirements-Mod-Not-found", Message.getString("requirements.mod.not-found"), "Requires mod version X but mod is not found on client");
    public final StringSetting requirementsModVersion = new StringSetting(this, "messages.Requirements-Mod-Version", Message.getString("requirements.mod.version"), "version X");
    public final StringSetting requirementsModVersionRange = new StringSetting(this, "messages.Requirements-Mod-Version-range", Message.getString("requirements.mod.version-range"), "version range X");

    public final StringSetting server_type = new StringSetting(this, "mohist.server-type", "FML", "Set the server type displayed in motd (FML/BUKKIT/VANILLA)");
    public final StringSetting lang = new StringSetting(this, "mohist.lang", "xx_XX", "Mohist internationalization language setting, will return the default system language when your settings are invalid");
    public final StringSetting console_name = new StringSetting(this, "mohist.console_name", "Server", "Front of the console, for example /say");
    public final BoolSetting support_nocmd = new BoolSetting(this, "mohist.support_nocmd", false, "Some server tools do not recognize I18N");
    // Bukkit Event Canceled
    public final BoolSetting explosion_canceled = new BoolSetting(this, "eventCanceled.explosion", false, "BlockExplosionEvent isCanceled");

    public final BoolSetting check_update = new BoolSetting(this, "mohist.check_update", true, "Check the version update status of the main program");
    public final BoolSetting needToUpdate = new BoolSetting(this, "mohist.check_update_auto_download", false, "Check new version will download automatically");
    public final BoolSetting check_libraries = new BoolSetting(this, "mohist.check_libraries", true, "Check libraries");

    public final StringSetting ANSI_ERROR_LEVEL = new StringSetting(this, "consolecolor.error-level", "[31;1m", "consolecolor.error-level");
    public final StringSetting ANSI_WARN_LEVEL = new StringSetting(this, "consolecolor.warn-level", "[33;1m", "consolecolor.warn-level");
    public final StringSetting ANSI_ERROR_MSG = new StringSetting(this, "consolecolor.error-msg", "[31;1m", "consolecolor.error-msg");
    public final StringSetting ANSI_WARN_MSG = new StringSetting(this, "consolecolor.warn-msg", "[33;1m", "consolecolor.warn-msg");

    public final BoolSetting disableForgeChunkForceSystem = new BoolSetting(this, "forge.disablechunkforcesystem", false, "Disable ForgeChunkForceSystem"); // by Goodvise
    public final BoolSetting stopserversaveworlds = new BoolSetting(this, "world.stopserversaveworlds", false, "stopserversaveworlds");
    public final BoolSetting disableannounceAdvancements = new BoolSetting(this, "disable-announce-Advancements", false, "Turn off achievment notifications");

    public final StringSetting modsblacklist = new StringSetting(this, "forge.modsblacklist.list", "aaaaa;bbbbbb", "mods black-list");
    public final StringSetting modsblacklistkickMessage = new StringSetting(this, "forge.modsblacklist.kickmessage", "Use of unauthorized mods", "mods black-list kick Message");

    public List<Integer> autounloadWorld_whitelist=new ArrayList();
    public final BoolSetting fakePlayerLogin = new BoolSetting(this, "fake-players.do-login", false, "Raise login events for fake players");
    
    public final BoolSetting pluginCheckBug = new BoolSetting(this, "plugin.promptBug", false, "Prompt for possible vulnerabilities of some plugins");// by lliioollcn
    public final BoolSetting CloseChatInConsole = new BoolSetting(this, "mohist.CloseChatInConsole", false, "Player's chat info will not be recorded in the console after opening");

    public final IntSetting minChunkLoadThreads = new IntSetting(this, "settings.min-chunk-load-threads",2,"Keep people from doing stupid things with max of 6");
    public final BoolSetting keepSpawnInMemory = new BoolSetting(this, "keep-spawn-loaded", true, "Keep spawn chunk loaded");
    public final BoolSetting RealTimeTicking = new BoolSetting(this, "mohist.realtimeticking", false, "RealTimeTicking from sponge");

    public final IntSetting entityTickLimit = new IntSetting(this, "entity-tick-limit", 300, "Entity maximum tick limit, entities exceeding this value will not be updated"); // by CraftDream
    public final StringSetting libraries_black_list = new StringSetting(this, "libraries_black_list", "aaaaa;bbbbbb", "libraries_black_list");

    public final BoolSetting hideJoinModsList = new BoolSetting(this, "hidejoinmodslist", false, "Hide the list of mods displayed on the console when players join the server");

    /* ======================================================================== */

    public MohistConfig()
    {
        super("mohist.yml");
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
                    if(setting == null) {
                        continue;
                    }
                    settings.put(setting.path, setting);
                }
                catch (ClassCastException e)
                {

                }
                catch(Throwable t)
                {
                    System.out.println("[Mohist] Failed to initialize a MohistConfig setting.");
                    t.printStackTrace();
                }
            }
        }
        load();
    }

    @Override
    public void load()
    {
        try
        {
            config = YamlConfiguration.loadConfiguration(configFile);
            StringBuilder header = new StringBuilder(HEADER + "\n");
            for (Setting toggle : settings.values())
            {
                if (!toggle.description.equals("")) {
                    header.append("Setting: ").append(toggle.path).append(" Default: ").append(toggle.def).append("   # ").append(toggle.description).append("\n");
                }

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(config.getString(toggle.path));
            }

            version = getInt("config-version", 2);
            set("config-version", 2);
            config.addDefault("forge.autounloadWorld_whitelist",new String[]{"0", "1", "-1"});
            this.autounloadWorld_whitelist=config.getIntegerList("forge.autounloadWorld_whitelist");

            config.options().header(header.toString());
            config.options().copyDefaults(true);
            this.save();
        }
        catch (Exception ex)
        {
            ServerAPI.getNMSServer().logSevere("Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }

    public boolean RealTimeTicking(){
        return RealTimeTicking.getValue();
    }
}
