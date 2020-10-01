package com.mohistmc.configuration;

import com.mohistmc.api.ServerAPI;
import com.mohistmc.util.i18n.Message;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spigotmc.SpigotConfig;

public class MohistConfig extends ConfigBase {

    private final String HEADER = "This is the main configuration file for Mohist.\n"
            + "\n"
            + "Home: https://mohist.red/\n";

    public static MohistConfig instance;

    /* ======================================================================== */

    public final StringSetting unknownCommandMessage = new StringSetting(this, "messages.use-unknow-command", Message.getString("use.unknow.command"), Message.getString("mohistsettings.unknownCommandMessage"));
    public final StringSetting outdatedClientMessage = new StringSetting(this, "messages.Outdate-Client", Message.getString("outdate.client"), Message.getString("mohistsettings.outdatedClientMessage"));
    public final StringSetting outdatedServerMessage = new StringSetting(this, "messages.Outdate-Server", Message.getString("outdate.server"), Message.getString("mohistsettings.outdatedServerMessage"));

    public final StringSetting rejectionsHackMessage = new StringSetting(this, "messages.Rejections-Hack", Message.getString("rejections.hack"), Message.getString("mohistsettings.rejectionsHackMessage"));
    public final StringSetting rejectionsServerModsMessage = new StringSetting(this, "messages.Rejections-Server-Mods", Message.getString("rejections.server-mods"), Message.getString("mohistsettings.rejectionsServerModsMessage"));

    public final StringSetting requirementsModInvalidVersion = new StringSetting(this, "messages.Requirements-Mod-Invalid-version", Message.getString("requirements.mod.invalid-version"), Message.getString("mohistsettings.requirementsModNotFound"));
    public final StringSetting requirementsModNotFound = new StringSetting(this, "messages.Requirements-Mod-Not-found", Message.getString("requirements.mod.not-found"), Message.getString("mohistsettings.requirementsModNotFound"));
    public final StringSetting requirementsModVersion = new StringSetting(this, "messages.Requirements-Mod-Version", Message.getString("requirements.mod.version"), Message.getString("mohistsettings.requirementsModVersion"));
    public final StringSetting requirementsModVersionRange = new StringSetting(this, "messages.Requirements-Mod-Version-range", Message.getString("requirements.mod.version-range"), Message.getString("mohistsettings.requirementsModVersionRange"));

    public final StringSetting server_type = new StringSetting(this, "mohist.server-type", "FML", Message.getString("mohistsettings.server_type"));
    public final StringSetting lang = new StringSetting(this, "mohist.lang", "xx_XX", Message.getString("mohistsettings.lang"));
    public final StringSetting console_name = new StringSetting(this, "mohist.console_name", "Server", Message.getString("mohistsettings.console_name"));
    public final BoolSetting support_nocmd = new BoolSetting(this, "mohist.support_nocmd", false, Message.getString("mohistsettings.support_nocmd"));
    // Bukkit Event Canceled
    public final BoolSetting explosion_canceled = new BoolSetting(this, "eventCanceled.explosion", false, Message.getString("mohistsettings.explosion_canceled"));

    public final BoolSetting use_custom_java8 = new BoolSetting(this, "mohist.use_custom_java8", false, Message.getString("mohistsettings.use_custom_java8"));
    public final BoolSetting check_update = new BoolSetting(this, "mohist.check_update", true, Message.getString("mohistsettings.check_update"));
    public final BoolSetting needToUpdate = new BoolSetting(this, "mohist.check_update_auto_download", false, Message.getString("mohistsettings.needToUpdate"));
    public final BoolSetting check_libraries = new BoolSetting(this, "mohist.check_libraries", true, Message.getString("mohistsettings.check_libraries"));
    public final BoolSetting disable_plugins_blacklist = new BoolSetting(this, "mohist.disable_plugins_blacklist", false, Message.getString("mohistsettings.disable_plugins_blacklist"));
    public final BoolSetting disable_mods_blacklist = new BoolSetting(this, "mohist.disable_mods_blacklist", false, Message.getString("mohistsettings.disable_mods_blacklist"));
    public final BoolSetting disable_config_update = new BoolSetting(this, "mohist.disable_config_update", false, Message.getString("mohistsettings.disable_config_update"));

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

    public final BoolSetting disableForgeChunkForceSystem = new BoolSetting(this, "forge.disablechunkforcesystem", false, Message.getString("mohistsettings.disableForgeChunkForceSystem")); // by Goodvise
    public final BoolSetting stopserversaveworlds = new BoolSetting(this, "world.stopserversaveworlds", false, Message.getString("mohistsettings.stopserversaveworlds"));
    public final BoolSetting disableannounceAdvancements = new BoolSetting(this, "disable-announce-Advancements", false, Message.getString("mohistsettings.disableannounceAdvancements"));

    public final StringSetting modsblacklist = new StringSetting(this, "forge.modswhitelist.list", "aaaa@version,bbbb@version", Message.getString("mohistsettings.modsblacklist"));
    public final StringSetting modsblacklistkickMessage = new StringSetting(this, "forge.modswhitelist.kickmessage", "Use of unauthorized mods", Message.getString("mohistsettings.modsblacklistkickMessage"));
    public final BoolSetting modswhitelistenable = new BoolSetting(this, "forge.enable_mods_whitelist", false, Message.getString("mohistsettings.modswhitelistenable"));
    public final IntSetting modsnumber = new IntSetting(this, "forge.whitelist_mods_number", 0, Message.getString("mohistsettings.whitelistmodsnumber"));
    public final IntSetting forgeversionmajor = new IntSetting(this, "forge.version.major", 14, "forge.version.major");
    public final IntSetting forgeversionminor = new IntSetting(this, "forge.version.minor", 23, "forge.version.minor");
    public final IntSetting forgeversionrevision = new IntSetting(this, "forge.version.revision", 5, "forge.version.revision");
    public final IntSetting forgeversionbuild = new IntSetting(this, "forge.version.build", 2854, "forge.version.build");


    public List<String> autounloadWorld_whitelist = new ArrayList();
    public final BoolSetting fakePlayerLogin = new BoolSetting(this, "fake-players.do-login", false, Message.getString("mohistsettings.fakePlayerLogin"));

    public final BoolSetting pluginCheckBug = new BoolSetting(this, "plugin.promptBug", false, Message.getString("mohistsettings.pluginCheckBug"));// by lliioollcn
    public final BoolSetting CloseChatInConsole = new BoolSetting(this, "mohist.CloseChatInConsole", false, Message.getString("mohistsettings.CloseChatInConsole"));

    public final IntSetting minChunkLoadThreads = new IntSetting(this, "settings.min-chunk-load-threads", 2, Message.getString("mohistsettings.minChunkLoadThreads"));
    public final BoolSetting keepSpawnInMemory = new BoolSetting(this, "keep-spawn-loaded", true, Message.getString("mohistsettings.keepSpawnInMemory"));
    public final BoolSetting RealTimeTicking = new BoolSetting(this, "mohist.realtimeticking", false, Message.getString("mohistsettings.RealTimeTicking"));
    public final BoolSetting FailOnUnresolvedGameProfile = new BoolSetting(this, "mohist.fail-on-unresolved-gameprofile", true, Message.getString("mohistsettings.FailOnUnresolvedGameProfile"));

    public final IntSetting entityTickLimit = new IntSetting(this, "entity-tick-limit", 300, Message.getString("mohistsettings.entityTickLimit")); // by CraftDream
    public final StringSetting libraries_black_list = new StringSetting(this, "libraries_black_list", "aaaaa;bbbbbb", Message.getString("mohistsettings.libraries_black_list"));

    public final BoolSetting hideJoinModsList = new BoolSetting(this, "hidejoinmodslist", false, Message.getString("mohistsettings.hideJoinModsList"));
    public final BoolSetting watchdog_spigot = new BoolSetting(this, "mohist.watchdog_spigot", true, "Open watchdog_spigot");
    public final BoolSetting watchdog_mohist = new BoolSetting(this, "mohist.watchdog_mohist", false, "Open watchdog_mohist");
    public final BoolSetting showlogo = new BoolSetting(this, "mohist.showlogo", true, "Show logo");

    public List<Integer> dimensionsNotLoaded =new ArrayList();
    /* ======================================================================== */

    public MohistConfig() {
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
                catch (ClassCastException ignored)
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
            config.addDefault("forge.autounloadWorld_whitelist", new String[]{"0", "1", "-1"});
            this.autounloadWorld_whitelist = config.getStringList("forge.autounloadWorld_whitelist");
            config.addDefault("world.dimensionsNotLoaded", new String[]{"1111111111111"});
            this.dimensionsNotLoaded = config.getIntegerList("world.dimensionsNotLoaded");
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

    public static String getHighlight(String key, String def) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        String color = yml.getString(key, def);
        return color;
    }

    public static void setValueMohist(String oldValue, String value) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        yml.set(oldValue, value);
        try {
            yml.save(MohistConfigUtil.mohistyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setValueMohist(String oldValue, boolean value) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        yml.set(oldValue, value);
        try {
            yml.save(MohistConfigUtil.mohistyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean bungeeOnlineMode = true;
    private static void bungeeOnlineMode() {
        bungeeOnlineMode = instance.getBoolean("mohist.bungee-online-mode", true);
    }

    public static boolean isProxyOnlineMode() {
        return Bukkit.getOnlineMode() || (SpigotConfig.bungee && bungeeOnlineMode);
    }
}
