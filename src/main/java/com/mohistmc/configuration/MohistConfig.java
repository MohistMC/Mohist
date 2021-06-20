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

    public static MohistConfig instance;
    public static boolean bungeeOnlineMode = true;

    /* ======================================================================== */
    public final StringSetting unknownCommandMessage = new StringSetting(this, "messages.use-unknow-command", Message.getString("use.unknow.command"));
    public final StringSetting outdatedClientMessage = new StringSetting(this, "messages.Outdate-Client", Message.getString("outdate.client"));
    public final StringSetting outdatedServerMessage = new StringSetting(this, "messages.Outdate-Server", Message.getString("outdate.server"));

    public final StringSetting rejectionsHackMessage = new StringSetting(this, "messages.Rejections-Hack", Message.getString("rejections.hack"));
    public final StringSetting rejectionsServerModsMessage = new StringSetting(this, "messages.Rejections-Server-Mods", Message.getString("rejections.server-mods"));

    public final StringSetting requirementsModInvalidVersion = new StringSetting(this, "messages.Requirements-Mod-Invalid-version", Message.getString("requirements.mod.invalid-version"));
    public final StringSetting requirementsModNotFound = new StringSetting(this, "messages.Requirements-Mod-Not-found", Message.getString("requirements.mod.not-found"));
    public final StringSetting requirementsModVersion = new StringSetting(this, "messages.Requirements-Mod-Version", Message.getString("requirements.mod.version"));
    public final StringSetting requirementsModVersionRange = new StringSetting(this, "messages.Requirements-Mod-Version-range", Message.getString("requirements.mod.version-range"));

    public final StringSetting server_type = new StringSetting(this, "mohist.server-type", "FML");
    public final StringSetting lang = new StringSetting(this, "mohist.lang", "xx_XX");
    public final StringSetting console_name = new StringSetting(this, "mohist.console_name", "Server");
    public final BoolSetting support_nocmd = new BoolSetting(this, "mohist.support_nocmd", false);
    // Bukkit Event Canceled
    public final BoolSetting explosion_canceled = new BoolSetting(this, "eventCanceled.explosion", false);
    public final BoolSetting keepInventory = new BoolSetting(this, "eventCanceled.keepInventory", false);
    public final BoolSetting keepLevel = new BoolSetting(this, "eventCanceled.keepLevel", false);

    public final BoolSetting use_custom_java8 = new BoolSetting(this, "mohist.use_custom_java8", false);
	public final BoolSetting forge_can_call_bukkit = new BoolSetting(this, "mohist.forge_can_call_bukkit", false);
    public final BoolSetting check_update = new BoolSetting(this, "mohist.check_update", true);
    public final BoolSetting needToUpdate = new BoolSetting(this, "mohist.check_update_auto_download", false);
    public final BoolSetting check_libraries = new BoolSetting(this, "mohist.check_libraries", true);
    public final BoolSetting downloadfile_command_enabled = new BoolSetting(this, "mohist.downloadfile_command_enabled", false);
    public final BoolSetting disable_plugins_blacklist = new BoolSetting(this, "mohist.disable_plugins_blacklist", false);
    public final BoolSetting disable_mods_blacklist = new BoolSetting(this, "mohist.disable_mods_blacklist", false);
    public final BoolSetting disable_config_update = new BoolSetting(this, "mohist.disable_config_update", false);

    public final StringSetting ANSI_ERROR_LEVEL = new StringSetting(this, "consolecolor.error-level", "c");
    public final StringSetting ANSI_WARN_LEVEL = new StringSetting(this, "consolecolor.warn-level", "e");
    public final StringSetting ANSI_INFO_LEVEL = new StringSetting(this, "consolecolor.info-level", "2");
    public final StringSetting ANSI_FATAL_LEVEL = new StringSetting(this, "consolecolor.fatal-level", "c");
    public final StringSetting ANSI_TRACE_LEVEL = new StringSetting(this, "consolecolor.trace-level", "c");

    public final StringSetting ANSI_ERROR_MSG = new StringSetting(this, "consolecolor.error-msg", "c");
    public final StringSetting ANSI_WARN_MSG = new StringSetting(this, "consolecolor.warn-msg", "e");
    public final StringSetting ANSI_INFO_MSG = new StringSetting(this, "consolecolor.info-msg", "f");
    public final StringSetting ANSI_FATAL_MSG = new StringSetting(this, "consolecolor.fatal-msg", "c");
    public final StringSetting ANSI_TRACE_MSG = new StringSetting(this, "consolecolor.trace-msg", "c");

    public final StringSetting ANSI_ERROR_TIME = new StringSetting(this, "consolecolor.error-time", "c");
    public final StringSetting ANSI_WARN_TIME = new StringSetting(this, "consolecolor.warn-time", "e");
    public final StringSetting ANSI_INFO_TIME = new StringSetting(this, "consolecolor.info-time", "b");
    public final StringSetting ANSI_FATAL_TIME = new StringSetting(this, "consolecolor.fatal-time", "c");
    public final StringSetting ANSI_TRACE_TIME = new StringSetting(this, "consolecolor.trace-time", "c");

    public final BoolSetting disableForgeChunkForceSystem = new BoolSetting(this, "forge.disablechunkforcesystem", false); // by Goodvise
    public final BoolSetting stopserversaveworlds = new BoolSetting(this, "world.stopserversaveworlds", false);
    public final BoolSetting disableannounceAdvancements = new BoolSetting(this, "disable-announce-Advancements", false);

    // mods black list
    public final StringSetting modsblacklist = new StringSetting(this, "forge.modsblacklist.list", "aaaa@version,bbbb@version");
    public final StringSetting modsblacklistkickMessage = new StringSetting(this, "forge.modsblacklist.kickmessage", "Use of unauthorized mods");
    public final BoolSetting modsblacklistenable = new BoolSetting(this, "forge.modsblacklist.enable", false);

    // mods white list
    public final IntSetting modsnumber = new IntSetting(this, "forge.modswhitelist.mods_number", 0);
    public final StringSetting modswhitelist = new StringSetting(this, "forge.modswhitelist.list", "minecraft@1.12.2,mcp@9.42,FML@8.0.99.99");
    public final StringSetting modswhitelistkickMessage = new StringSetting(this, "forge.modswhitelist.kickmessage", "Use of unauthorized mods");
    public final BoolSetting modswhitelistenable = new BoolSetting(this, "forge.modswhitelist.enable", false);


    public final IntSetting forgeversionmajor = new IntSetting(this, "forge.version.major", 14);
    public final IntSetting forgeversionminor = new IntSetting(this, "forge.version.minor", 23);
    public final IntSetting forgeversionrevision = new IntSetting(this, "forge.version.revision", 5);
    public final IntSetting forgeversionbuild = new IntSetting(this, "forge.version.build", 2855);
    public final BoolSetting autounloadworldenable = new BoolSetting(this, "forge.autounloadworld.enable", false);
    public final BoolSetting fakePlayerLogin = new BoolSetting(this, "fake-players.do-login", false);
    public final BoolSetting CloseChatInConsole = new BoolSetting(this, "mohist.CloseChatInConsole", false);
    public final IntSetting minChunkLoadThreads = new IntSetting(this, "settings.min-chunk-load-threads", 2);
    public final BoolSetting keepSpawnInMemory = new BoolSetting(this, "keep-spawn-loaded", true);
    public final BoolSetting RealTimeTicking = new BoolSetting(this, "mohist.realtimeticking", false);
    public final BoolSetting FailOnUnresolvedGameProfile = new BoolSetting(this, "mohist.fail-on-unresolved-gameprofile", true);
    public final IntSetting entityTickLimit = new IntSetting(this, "entity-tick-limit", 300); // by CraftDream
    public final StringSetting libraries_black_list = new StringSetting(this, "libraries_black_list", "aaaaa;bbbbbb");
    public final BoolSetting hideJoinModsList = new BoolSetting(this, "hidejoinmodslist", false);
    public final BoolSetting watchdog_spigot = new BoolSetting(this, "mohist.watchdog_spigot", true);
    public final BoolSetting watchdog_mohist = new BoolSetting(this, "mohist.watchdog_mohist", false);
    public final BoolSetting showlogo = new BoolSetting(this, "mohist.showlogo", true);
    public final BoolSetting World_Directory_Client = new BoolSetting(this, "world.directory_in_client", true);
    public final BoolSetting bukkitPermissionsHandler = new BoolSetting(this, "mohist.BukkitPermissionsHandler", true);
    private final String HEADER = "This is the main configuration file for Mohist.\n"
            + "\n"
            + "Home: https://mohist.red/\n";
    public List<String> autounloadworld_whitelist = new ArrayList();
    /* ======================================================================== */
    public List<Integer> dimensionsNotLoaded = new ArrayList();

    public final IntSetting connectionTimeout = new IntSetting(this, "mohist.connectionTimeout", 15000);

    public MohistConfig() {
        super("mohist.yml");
        init();
        instance = this;
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

    private static void bungeeOnlineMode() {
        bungeeOnlineMode = instance.getBoolean("mohist.bungee-online-mode", true);
    }

    public static boolean isProxyOnlineMode() {
        return Bukkit.getOnlineMode() || (SpigotConfig.bungee && bungeeOnlineMode);
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
                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(config.getString(toggle.path));
            }

            version = getInt("config-version", 3);
            set("config-version", 3);
            config.addDefault("forge.autounloadworld.whitelist", new String[]{"0", "1", "-1"});
            this.autounloadworld_whitelist = config.getStringList("forge.autounloadworld.whitelist");
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
