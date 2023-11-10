package com.mohistmc;

import com.google.common.base.Throwables;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.api.color.ColorsAPI;
import com.mohistmc.commands.BackupWorldCommand;
import com.mohistmc.commands.BansCommand;
import com.mohistmc.commands.DumpCommand;
import com.mohistmc.commands.EntityCommand;
import com.mohistmc.commands.ItemsCommand;
import com.mohistmc.commands.MohistCommand;
import com.mohistmc.commands.PermissionCommand;
import com.mohistmc.commands.PingCommand;
import com.mohistmc.commands.PluginCommand;
import com.mohistmc.commands.ShowsCommand;
import com.mohistmc.plugins.warps.WarpsCommands;
import com.mohistmc.plugins.world.commands.WorldsCommands;
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
        commands.put("dump", new DumpCommand("dump"));
        commands.put("plugin", new PluginCommand("plugin"));
        commands.put("backupworld", new BackupWorldCommand("backupworld"));
        commands.put("items", new ItemsCommand("items"));
        commands.put("permission", new PermissionCommand("permission"));
        commands.put("bans", new BansCommand("bans"));
        commands.put("shows", new ShowsCommand("shows"));
        commands.put("ping", new PingCommand("ping"));
        commands.put("entity", new EntityCommand("entity"));

        if (getBoolean("worldmanage", true)) {
            commands.put("worlds", new WorldsCommands("worlds"));
        }
        commands.put("warps", new WarpsCommands("warps"));

        version = getInt("config-version", 1);
        set("config-version", 1);
        set("keepinventory.world.inventory", false);
        set("keepinventory.world.exp", false);
        readConfig();
    }

    public static void save() {
        readConfig();
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

    public static void set(String path, Object val) {
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

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
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

    public static String networkmanager_enable() {
        return yml.getString("networkmanager.enable", Locale.getDefault().toString());
    }

    public static boolean show_logo;
    public static String mohist_lang;
    public static boolean check_update;
    public static int maximumRepairCost;
    public static boolean enchantment_fix;
    public static int max_enchantment_level;

    public static boolean modlist_check_blacklist_enable;
    public static List<String> modlist_check_blacklist;
    public static String modlist_check_blacklist_message;

    public static boolean modlist_check_whitelist_enable;
    public static String modlist_check_whitelist;
    public static String modlist_check_whitelist_message;
    public static int maxBees;
    public static boolean bookAnimationTick;
    public static boolean networkmanager_enable;
    public static boolean networkmanager_debug;
    public static List<String> networkmanager_intercept;
    public static boolean keepinventory_global;
    public static boolean keepinventory_inventory;
    public static boolean keepinventory_exp;

    // Thread Priority
    public static int server_thread;

    public static boolean clear_item;
    public static List<String> clear_item__whitelist;
    public static String clear_item__msg;
    public static int clear_item__time;

    // Ban
    public static boolean ban_item_enable;
    public static List<String> ban_item_materials;
    public static boolean ban_entity_enable;
    public static List<String> ban_entity_types;

    public static boolean ban_enchantment_enable;
    public static List<String> ban_enchantment_list;

    public static String motdFirstLine;
    public static String motdSecondLine;
    public static String pingCommandOutput;

    // Ban events
    public static boolean doFireTick;
    public static boolean worldmanage;

    public static boolean bukkitpermissionshandler;

    public static String serverbranding;
    public static boolean velocity_enabled;
    public static boolean velocity_onlineMode;
    public static String velocity_secret;
    @Deprecated(forRemoval = true, since = "1.21")
    public static boolean ignoreConnectionType;

    private static void mohist() {
        show_logo = getBoolean("mohist.show_logo", true);
        mohist_lang = getString("mohist.lang", Locale.getDefault().toString());
        check_update = getBoolean("mohist.check_update", true);
        maximumRepairCost = getInt("anvilfix.maximumrepaircost", 40);
        enchantment_fix = getBoolean("anvilfix.enchantment_fix", false);
        max_enchantment_level = getInt("anvilfix.max_enchantment_level", 32767);
        modlist_check_blacklist_enable = getBoolean("modlist_check.blacklist.enable", false);
        modlist_check_blacklist = getList("modlist_check.blacklist.list", new ArrayList<>());
        modlist_check_blacklist_message = getString("modlist_check.blacklist.message", "Connection closed - PlayerModsCheck blacklist");
        modlist_check_whitelist_enable = getBoolean("modlist_check.whitelist.enable", false);
        modlist_check_whitelist = getString("modlist_check.whitelist.list", ServerAPI.modlists_All.toString().replace(", mohist", ""));
        modlist_check_whitelist_message = getString("modlist_check.whitelist.message", "Connection closed - PlayerModsCheck whitelist");
        maxBees = getInt("max-bees-in-hive", 3);
        bookAnimationTick = getBoolean("enchantment-table-book-animation-tick", false);
        networkmanager_debug = getBoolean("networkmanager.enable", false);
        networkmanager_debug = getBoolean("networkmanager.debug", false);
        networkmanager_intercept = getList("networkmanager.intercept", new ArrayList<>());
        keepinventory_global = getBoolean("keepinventory.global.enable", false);
        keepinventory_inventory = getBoolean("keepinventory.global.inventory", true);
        keepinventory_exp = getBoolean("keepinventory.global.exp", true);
        server_thread = getInt("threadpriority.server_thread", 8);

        clear_item = getBoolean("entity.clear.item.enable", false);
        clear_item__whitelist = getList("entity.clear.item.whitelist", new ArrayList<>());
        clear_item__msg = getString("entity.clear.item.msg", "[Server] Cleaned up %size% drops");
        clear_item__time = getInt("entity.clear.item.time", 1800);

        ban_item_enable = getBoolean("ban.item.enable" , false);
        ban_item_materials = getList("ban.item.list", new ArrayList<>());
        ban_entity_enable = getBoolean("ban.entity.enable", false);
        ban_entity_types = getList("ban.entity.list", new ArrayList<>());
        ban_enchantment_enable = getBoolean("ban.enchantment.enable", false);
        ban_enchantment_list = getList("ban.enchantment.list", new ArrayList<>());
        motdFirstLine = ColorsAPI.of(getString("motd.firstline", "<RAINBOW1>A Minecraft Server</RAINBOW>"));
        motdSecondLine = ColorsAPI.of(getString("motd.secondline", ""));

        pingCommandOutput = getString("settings.messages.ping-command-output", "§2%s's ping is %sms");

        doFireTick = getBoolean("events.fire_tick", false);
        bukkitpermissionshandler = getBoolean("forge.bukkitpermissionshandler", true);
        worldmanage = getBoolean("worldmanage", true);
        serverbranding = ColorsAPI.of(getString("server_branding", MohistMC.modid));
        velocity_enabled = getBoolean("velocity.enabled", false);
        velocity_onlineMode = getBoolean("velocity.onlineMode", false);
        velocity_secret = getString("velocity.secret", "");
        ignoreConnectionType = getBoolean("forge.ignoreConnectionType", false);
    }
}