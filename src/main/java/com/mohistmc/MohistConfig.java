package com.mohistmc;

import com.google.common.base.Throwables;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.api.color.ColorsAPI;
import com.mohistmc.commands.BackupWorldCommand;
import com.mohistmc.commands.BansCommand;
import com.mohistmc.commands.DumpCommand;
import com.mohistmc.commands.GetPluginListCommand;
import com.mohistmc.commands.ItemsCommand;
import com.mohistmc.commands.MohistCommand;
import com.mohistmc.commands.PermissionCommand;
import com.mohistmc.commands.PingCommand;
import com.mohistmc.commands.PluginCommand;
import com.mohistmc.commands.ShowsCommand;
import com.mohistmc.plugins.MohistPlugin;
import com.mohistmc.util.YamlUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class MohistConfig {

    private static final List<String> HEADER = Arrays.asList("""
            This is the main configuration file for Mohist.
            As you can see, there's tons to configure. Some options may impact gameplay, so use
            with caution, and make sure you know what each option does before configuring.
            For a reference for any variable inside this file, check out the Mohist wiki at
            https://wiki.mohistmc.com/

            If you need help with the configuration or have any questions related to Spigot,
            join us at the Discord or drop by our forums and leave a post.

            Discord: https://discord.gg/mohistmc
            Forums: https://mohistmc.com/
            Forums (CN): https://mohistmc.cn/
                        
            """.split("\\n"));
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    private static File CONFIG_FILE;

    public static File mohistyml = new File("mohist-config", "mohist.yml");
    public static YamlConfiguration yml = YamlConfiguration.loadConfiguration(mohistyml);

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load mohist.yml, please correct your syntax errors", ex);
            Throwables.throwIfUnchecked(ex);
        }

        config.options().setHeader(HEADER);
        config.options().copyDefaults(true);

        commands = new HashMap<>();
        commands.put("mohist", new MohistCommand("mohist"));
        commands.put("getpluginlist", new GetPluginListCommand("getpluginlist"));
        commands.put("dump", new DumpCommand("dump"));
        commands.put("plugin", new PluginCommand("plugin"));
        commands.put("backupworld", new BackupWorldCommand("backupworld"));
        commands.put("items", new ItemsCommand("items"));
        commands.put("permission", new PermissionCommand("permission"));
        commands.put("bans", new BansCommand("bans"));
        commands.put("shows", new ShowsCommand("shows"));
        commands.put("ping", new PingCommand("ping"));

        MohistPlugin.registerCommands(commands);

        version = getInt("config-version", 1);
        set("config-version", 1);
        set("keepinventory.world.inventory", false);
        set("keepinventory.world.exp", false);
        readConfig();

        try {
            Class.forName("org.sqlite.JDBC");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Throwable t) {
            throw new RuntimeException("Error initializing Mohist", t);
        }
    }

    public static void save() {
        YamlUtils.save(mohistyml, yml);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Mohist", entry.getValue());
        }
    }

    static void readConfig() {
        for (Method method : MohistConfig.class.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(null);
                    } catch (InvocationTargetException ex) {
                        Throwables.throwIfUnchecked(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List<String> getStringList(String path, T def) {
        config.addDefault(path, def);
        return config.getStringList(path);
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    public static String mohist_lang() {
        return yml.getString("mohist.lang", Locale.getDefault().toString());
    }
    public static String motd() {
        return ColorsAPI.of(MohistConfig.motdFirstLine) + "\n" + ColorsAPI.of(MohistConfig.motdSecondLine);
    }

    public static boolean isProxyOnlineMode() {
        return org.bukkit.Bukkit.getOnlineMode()  || (velocity_enabled && velocity_onlineMode);
    }

    public static boolean show_logo;
    public static String mohist_lang;
    public static boolean check_update;
    public static int maximumRepairCost;
    public static boolean enchantment_fix;
    public static int max_enchantment_level;

    public static boolean player_modlist_blacklist_enable;
    public static List<String> player_modlist_blacklist;

    public static boolean server_modlist_whitelist_enable;
    public static String server_modlist_whitelist;
    public static int maxBees;
    public static boolean bookAnimationTick;
    public static boolean networkmanager_debug;
    public static List<String> networkmanager_intercept;
    public static boolean keepinventory_global;
    public static boolean keepinventory_inventory;
    public static String keepinventory_inventory_permission;
    public static boolean keepinventory_exp;
    public static String keepinventory_exp_permission;

    // Thread Priority
    public static int server_thread;

    public static boolean clear_item;
    public static List<String> clear_item_whitelist;
    public static String clear_item_msg;
    public static int clear_item_time;

    public static boolean clear_monster;
    public static List<String> clear_monster_whitelist;
    public static String clear_monster_msg;
    public static int clear_monster_time;

    // Ban
    public static boolean ban_item_enable;
    public static List<String> ban_item_materials;
    public static boolean ban_entity_enable;
    public static List<String> ban_entity_types;

    public static boolean ban_enchantment_enable;
    public static List<String> ban_enchantment_list;

    public static boolean motdEnable;
    public static String motdFirstLine;
    public static String motdSecondLine;
    public static String pingCommandOutput;

    // Ban events
    public static boolean doFireTick;
    public static boolean explosion;

    public static boolean worldmanage;

    public static boolean bukkitpermissionshandler;
    public static boolean velocity_enabled;
    public static boolean velocity_onlineMode;
    public static String velocity_secret;

    public static boolean recipe_warn;

    public static boolean tpa_enable;
    public static boolean tpa_permissions_enable;
    public static boolean back_enable;
    public static boolean back_permissions_enable;
    public static boolean permissions_debug_console;
    public static boolean permissions_send_player;

    public static boolean watchdog_spigot;
    public static boolean watchdog_mohist;

    private static void mohist() {
        show_logo = getBoolean("mohist.show_logo", true);
        mohist_lang = getString("mohist.lang", Locale.getDefault().toString());
        check_update = getBoolean("mohist.check_update", true);
        watchdog_spigot = getBoolean("mohist.watchdog_spigot", true);
        watchdog_mohist = getBoolean("mohist.watchdog_mohist", false);
        maximumRepairCost = getInt("anvilfix.maximumrepaircost", 40);
        enchantment_fix = getBoolean("anvilfix.enchantment_fix", false);
        max_enchantment_level = getInt("anvilfix.max_enchantment_level", 32767);
        player_modlist_blacklist_enable = getBoolean("player_modlist_blacklist.enable", false);
        player_modlist_blacklist = getStringList("player_modlist_blacklist.list", new ArrayList<>());
        server_modlist_whitelist_enable = getBoolean("server_modlist_whitelist.enable", false);
        server_modlist_whitelist = getString("server_modlist_whitelist.list", ServerAPI.modlists_All.toString().replace(", mohist", ""));
        maxBees = getInt("max-bees-in-hive", 3);
        bookAnimationTick = getBoolean("enchantment-table-book-animation-tick", false);
        networkmanager_debug = getBoolean("networkmanager.debug", false);
        networkmanager_intercept = getStringList("networkmanager.intercept", new ArrayList<>());
        keepinventory_global = getBoolean("keepinventory.global.enable", false);
        keepinventory_inventory = getBoolean("keepinventory.global.inventory", true);
        keepinventory_inventory_permission = getString("keepinventory.permission.inventory", "mohist.keepinventory.inventory");
        keepinventory_exp = getBoolean("keepinventory.global.exp", true);
        keepinventory_exp_permission = getString("keepinventory.permission.exp", "mohist.keepinventory.exp");
        server_thread = getInt("threadpriority.server_thread", 8);

        clear_item = getBoolean("entity.clear.item.enable", false);
        clear_item_whitelist = getStringList("entity.clear.item.whitelist", new ArrayList<>());
        clear_item_msg = getString("entity.clear.item.msg", "[Server] Cleaned up %size% drop item");
        clear_item_time = getInt("entity.clear.item.time", 1800);

        clear_monster = getBoolean("entity.clear.monster.enable", false);
        clear_monster_whitelist = getStringList("entity.clear.monster.whitelist", new ArrayList<>());
        clear_monster_msg = getString("entity.clear.monster.msg", "[Server] Cleaned up %size% monster");
        clear_monster_time = getInt("entity.clear.monster.time", 1800);

        ban_item_enable = getBoolean("ban.item.enable" , false);
        ban_item_materials = getStringList("ban.item.list", new ArrayList<>());
        ban_entity_enable = getBoolean("ban.entity.enable", false);
        ban_entity_types = getStringList("ban.entity.list", new ArrayList<>());
        ban_enchantment_enable = getBoolean("ban.enchantment.enable", false);
        ban_enchantment_list = getStringList("ban.enchantment.list", new ArrayList<>());
        motdEnable = getBoolean("motd.enable", false);
        motdFirstLine = getString("motd.firstline", "<RAINBOW1>A Minecraft Server</RAINBOW>");
        motdSecondLine = getString("motd.secondline", "");

        pingCommandOutput = getString("settings.messages.ping-command-output", "§2%s's ping is %sms");

        doFireTick = getBoolean("events.fire_tick", false);
        explosion = getBoolean("events.explosion", false);
        bukkitpermissionshandler = getBoolean("forge.bukkitpermissionshandler", true);
        worldmanage = getBoolean("worldmanage", true);
        velocity_enabled = getBoolean("velocity.enabled", false);
        velocity_onlineMode = getBoolean("velocity.onlineMode", false);
        velocity_secret = getString("velocity.secret", "");

        recipe_warn = getBoolean("recipe.warn", false);
        tpa_enable = getBoolean("tpa.enable", false);
        tpa_permissions_enable = getBoolean("tpa.permissions", true);
        back_enable = getBoolean("back.enable", false);
        back_permissions_enable = getBoolean("back.permissions", true);

        permissions_debug_console = getBoolean("permissions.debug.console", false);
        permissions_send_player = getBoolean("permissions.debug.player", false);
    }
}