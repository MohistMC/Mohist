package red.mohist.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.api.ServerAPI;
import red.mohist.util.i18n.I18N;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class MohistConfig extends ConfigBase {

    private final String HEADER = "This is the main configuration file for Mohist.\n"
            + "\n"
            + "Home: https://mohist.red/\n";

    public static MohistConfig instance;

    /* ======================================================================== */

    public final StringSetting unknownCommandMessage = new StringSetting(this, "messages.use-unknow-command", I18N.get("use.unknow.command"), I18N.get("mohistsettings.unknownCommandMessage"));
    public final StringSetting outdatedClientMessage = new StringSetting(this, "messages.Outdate-Client", I18N.get("outdate.client"), I18N.get("mohistsettings.outdatedClientMessage"));
    public final StringSetting outdatedServerMessage = new StringSetting(this, "messages.Outdate-Server", I18N.get("outdate.server"), I18N.get("mohistsettings.outdatedServerMessage"));

    public final StringSetting rejectionsHackMessage = new StringSetting(this, "messages.Rejections-Hack", I18N.get("rejections.hack"), I18N.get("mohistsettings.rejectionsHackMessage"));
    public final StringSetting rejectionsServerModsMessage = new StringSetting(this, "messages.Rejections-Server-Mods", I18N.get("rejections.server-mods"), I18N.get("mohistsettings.rejectionsServerModsMessage"));

    public final StringSetting requirementsModInvalidVersion = new StringSetting(this, "messages.Requirements-Mod-Invalid-version", I18N.get("requirements.mod.invalid-version"), I18N.get("mohistsettings.requirementsModNotFound"));
    public final StringSetting requirementsModNotFound = new StringSetting(this, "messages.Requirements-Mod-Not-found", I18N.get("requirements.mod.not-found"), I18N.get("mohistsettings.requirementsModNotFound"));
    public final StringSetting requirementsModVersion = new StringSetting(this, "messages.Requirements-Mod-Version", I18N.get("requirements.mod.version"), I18N.get("mohistsettings.requirementsModVersion"));
    public final StringSetting requirementsModVersionRange = new StringSetting(this, "messages.Requirements-Mod-Version-range", I18N.get("requirements.mod.version-range"), I18N.get("mohistsettings.requirementsModVersionRange"));

    public final StringSetting server_type = new StringSetting(this, "mohist.server-type", "FML", I18N.get("mohistsettings.server_type"));
    public final StringSetting lang = new StringSetting(this, "mohist.lang", "xx_XX", I18N.get("mohistsettings.lang"));
    public final StringSetting console_name = new StringSetting(this, "mohist.console_name", "Server", I18N.get("mohistsettings.console_name"));
    public final BoolSetting support_nocmd = new BoolSetting(this, "mohist.support_nocmd", false, I18N.get("mohistsettings.support_nocmd"));
    // Bukkit Event Canceled
    public final BoolSetting explosion_canceled = new BoolSetting(this, "eventCanceled.explosion", false, I18N.get("mohistsettings.explosion_canceled"));

    public final BoolSetting use_custom_java8 = new BoolSetting(this, "mohist.use_custom_java8", false, I18N.get("mohistsettings.use_custom_java8"));
    public final BoolSetting check_update = new BoolSetting(this, "mohist.check_update", true, I18N.get("mohistsettings.check_update"));
    public final BoolSetting needToUpdate = new BoolSetting(this, "mohist.check_update_auto_download", false, I18N.get("mohistsettings.needToUpdate"));
    public final BoolSetting check_libraries = new BoolSetting(this, "mohist.check_libraries", true, I18N.get("mohistsettings.check_libraries"));
    public final BoolSetting disable_plugins_blacklist = new BoolSetting(this, "mohist.disable_plugins_blacklist", false, I18N.get("mohistsettings.disable_plugins_blacklist"));
    public final BoolSetting disable_mods_blacklist = new BoolSetting(this, "mohist.disable_mods_blacklist", false, I18N.get("mohistsettings.disable_mods_blacklist"));

    public final StringSetting ANSI_ERROR_LEVEL = new StringSetting(this, "consolecolor.error-level", "c", "consolecolor.error-level");
    public final StringSetting ANSI_WARN_LEVEL = new StringSetting(this, "consolecolor.warn-level", "e", "consolecolor.warn-level");
    public final StringSetting ANSI_INFO_LEVEL = new StringSetting(this, "consolecolor.info-level", "2", "consolecolor.info-level");
    public final StringSetting ANSI_FATAL_LEVEL = new StringSetting(this, "consolecolor.fatal-level", "c", "consolecolor.fatal-level");
    public final StringSetting ANSI_TRACE_LEVEL = new StringSetting(this, "consolecolor.trace-level", "c", "consolecolor.trace-level");

    public final StringSetting ANSI_ERROR_MSG = new StringSetting(this, "consolecolor.error-msg", "c", "consolecolor.error-msg");
    public final StringSetting ANSI_WARN_MSG = new StringSetting(this, "consolecolor.warn-msg", "e", "consolecolor.warn-msg");
    public final StringSetting ANSI_INFO_MSG = new StringSetting(this, "consolecolor.info-msg", "f", "consolecolor.info-msg");
    public final StringSetting ANSI_FATAL_MSG = new StringSetting(this, "consolecolor.fatal-msg", "c", "consolecolor.fatal-msg");
    public final StringSetting ANSI_TRACE_MSG = new StringSetting(this, "consolecolor.trace-msg", "c", "consolecolor.trace-msg");

    public final StringSetting ANSI_ERROR_TIME = new StringSetting(this, "consolecolor.error-time", "b", "consolecolor.warn-time");
    public final StringSetting ANSI_WARN_TIME = new StringSetting(this, "consolecolor.warn-time", "b", "consolecolor.warn-time");
    public final StringSetting ANSI_INFO_TIME = new StringSetting(this, "consolecolor.info-time", "b", "consolecolor.info-time");
    public final StringSetting ANSI_FATAL_TIME = new StringSetting(this, "consolecolor.fatal-time", "b", "consolecolor.fatal-time");
    public final StringSetting ANSI_TRACE_TIME = new StringSetting(this, "consolecolor.trace-time", "b", "consolecolor.trace-time");

    public final BoolSetting disableForgeChunkForceSystem = new BoolSetting(this, "forge.disablechunkforcesystem", false, I18N.get("mohistsettings.disableForgeChunkForceSystem")); // by Goodvise
    public final BoolSetting stopserversaveworlds = new BoolSetting(this, "world.stopserversaveworlds", false, I18N.get("mohistsettings.stopserversaveworlds"));
    public final BoolSetting disableannounceAdvancements = new BoolSetting(this, "disable-announce-Advancements", false, I18N.get("mohistsettings.disableannounceAdvancements"));

    public final StringSetting modsblacklist = new StringSetting(this, "forge.modswhitelist.list", "aaaa@version,bbbb@version", I18N.get("mohistsettings.modsblacklist"));
    public final StringSetting modsblacklistkickMessage = new StringSetting(this, "forge.modswhitelist.kickmessage", "Use of unauthorized mods", I18N.get("mohistsettings.modsblacklistkickMessage"));
    public final BoolSetting modswhitelistenable = new BoolSetting(this, "forge.enable_mods_whitelist", false, I18N.get("mohistsettings.modswhitelistenable"));
    public final IntSetting modsnumber = new IntSetting(this, "forge.whitelist_mods_number", 0, I18N.get("mohistsettings.whitelistmodsnumber"));
    public final IntSetting forgeversionmajor = new IntSetting(this, "forge.version.major", 14, "forge.version.major");
    public final IntSetting forgeversionminor = new IntSetting(this, "forge.version.minor", 23, "forge.version.minor");
    public final IntSetting forgeversionrevision = new IntSetting(this, "forge.version.revision", 5, "forge.version.revision");
    public final IntSetting forgeversionbuild = new IntSetting(this, "forge.version.build", 2854, "forge.version.build");


    public List<String> autounloadWorld_whitelist = new ArrayList();
    public final BoolSetting fakePlayerLogin = new BoolSetting(this, "fake-players.do-login", false, I18N.get("mohistsettings.fakePlayerLogin"));

    public final BoolSetting pluginCheckBug = new BoolSetting(this, "plugin.promptBug", false, I18N.get("mohistsettings.pluginCheckBug"));// by lliioollcn
    public final BoolSetting CloseChatInConsole = new BoolSetting(this, "mohist.CloseChatInConsole", false, I18N.get("mohistsettings.CloseChatInConsole"));

    public final IntSetting minChunkLoadThreads = new IntSetting(this, "settings.min-chunk-load-threads", 2, I18N.get("mohistsettings.minChunkLoadThreads"));
    public final BoolSetting keepSpawnInMemory = new BoolSetting(this, "keep-spawn-loaded", true, I18N.get("mohistsettings.keepSpawnInMemory"));
    public final BoolSetting RealTimeTicking = new BoolSetting(this, "mohist.realtimeticking", false, I18N.get("mohistsettings.RealTimeTicking"));
    public final BoolSetting FailOnUnresolvedGameProfile = new BoolSetting(this, "mohist.fail-on-unresolved-gameprofile", true, I18N.get("mohistsettings.FailOnUnresolvedGameProfile"));

    public final IntSetting entityTickLimit = new IntSetting(this, "entity-tick-limit", 300, I18N.get("mohistsettings.entityTickLimit")); // by CraftDream
    public final StringSetting libraries_black_list = new StringSetting(this, "libraries_black_list", "aaaaa;bbbbbb", I18N.get("mohistsettings.libraries_black_list"));

    public final BoolSetting hideJoinModsList = new BoolSetting(this, "hidejoinmodslist", false, I18N.get("mohistsettings.hideJoinModsList"));

    public List<Integer> dimensionsNotLoaded = new ArrayList();
    /* ======================================================================== */

    public MohistConfig() {
        super("mohist.yml");
        init();
        instance = this;
    }

    public void init() {
        for (Field f : this.getClass().getFields()) {
            if (Modifier.isFinal(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers())) {
                try {
                    Setting setting = (Setting) f.get(this);
                    if (setting == null) {
                        continue;
                    }
                    settings.put(setting.path, setting);
                } catch (ClassCastException ignored) {

                } catch (Throwable t) {
                    System.out.println("[Mohist] Failed to initialize a MohistConfig setting.");
                    t.printStackTrace();
                }
            }
        }
        load();
    }

    @Override
    public void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            StringBuilder header = new StringBuilder(HEADER + "\n");
            for (Setting toggle : settings.values()) {
                if (!toggle.description.equals("")) {
                    header.append("Setting: ").append(toggle.path).append(" Default: ").append(toggle.def).append("   # ").append(toggle.description).append("\n");
                }

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(config.getString(toggle.path));
            }

            version = getInt("config-version", 2);
            set("config-version", 2);
            config.addDefault("forge.autounloadWorld_whitelist", new String[]{"0", "1", "-1"});
            this.autounloadWorld_whitelist = config.getStringList("forge.autounloadWorld_whitelist");
            config.addDefault("world.dimensionsNotLoaded", new String[]{"1111111111111"});
            this.dimensionsNotLoaded = config.getIntegerList("world.dimensionsNotLoaded");
            config.options().header(header.toString());
            config.options().copyDefaults(true);
            this.save();
        } catch (Exception ex) {
            ServerAPI.getNMSServer().logSevere("Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }

    public boolean RealTimeTicking() {
        return RealTimeTicking.getValue();
    }

}
