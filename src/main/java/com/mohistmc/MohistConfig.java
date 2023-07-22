package com.mohistmc;

import com.google.common.base.Throwables;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.command.BackupWorldCommand;
import com.mohistmc.command.BiomeCommand;
import com.mohistmc.command.DownloadFileCommand;
import com.mohistmc.command.DumpCommand;
import com.mohistmc.command.GetPluginListCommand;
import com.mohistmc.command.MohistCommand;
import com.mohistmc.command.PluginCommand;
import com.mohistmc.command.UpdateMohistCommand;
import com.mohistmc.command.WhitelistModsCommand;
import com.mohistmc.plugins.WorldCommand;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class MohistConfig {

    private static final String HEADER =
            "This is the main configuration file for Mohist."  + "\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use "  + "\n"
            + "with caution, and make sure you know what each option does before configuring."  + "\n"
            + "For a reference for any variable inside this file, check out the Mohist wiki at"  + "\n"
            + "https://wiki.mohistmc.com/"  + "\n"

            + "If you need help with the configuration or have any questions related to Spigot,"  + "\n"
            + "join us at the Discord or drop by our forums and leave a post."  + "\n"

            + "Discord: https://discord.gg/mohistmc"  + "\n"
            + "Forums: https://mohistmc.com/"  + "\n"
            + "Forums (CN): https://mohistmc.cn/"  + "\n";

    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    private static File CONFIG_FILE;

    public static File mohistyml = new File("mohist-config", "mohist.yml");
    public static YamlConfiguration yml = YamlConfiguration.loadConfiguration(mohistyml);

    public static void save()  {
        try {
            yml.save(mohistyml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load mohist.yml, please correct your syntax errors", ex);
            Throwables.throwIfUnchecked(ex);
        }

        config.options().header(HEADER);
        config.options().copyDefaults(true);

        commands = new HashMap<>();

        // Mohist
        commands.put("mohist", new MohistCommand("mohist"));
        commands.put("mohist", new GetPluginListCommand("getpluginlist"));
        commands.put("mohist", new DownloadFileCommand("downloadfile"));
        commands.put("mohist", new DumpCommand("dump"));
        commands.put("mohist", new PluginCommand("plugin"));
        commands.put("mohist", new UpdateMohistCommand("updatemohist"));
        commands.put("mohist", new BackupWorldCommand("backupworld"));
        commands.put("mohist", new WhitelistModsCommand("whitelistmods"));
        commands.put("mohist", new WorldCommand("world"));
        commands.put("mohist", new BiomeCommand("biome"));

        version = getInt("config-version", 1);
        set("config-version", 1);
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

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return (List<T>) config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    public static boolean show_logo;
    public static String mohist_lang;
    public static String mohist_vanilla_lang;
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
    public static boolean hideJoinModsList;
    public static boolean check_update_auto_download;

    private static void mohist() {
        show_logo = getBoolean("mohist.show_logo", true);
        mohist_lang = getString("mohist.lang", "xx_XX");
        mohist_vanilla_lang = getString("mohist.vanilla_lang", "en_us");
        check_update = getBoolean("mohist.check_update", true);
        maximumRepairCost = getInt("anvilfix.maximumrepaircost", 40);
        enchantment_fix = getBoolean("anvilfix.enchantment_fix", false);
        max_enchantment_level = getInt("anvilfix.max_enchantment_level", 32767);
        player_modlist_blacklist_enable = getBoolean("player_modlist_blacklist.enable", false);
        player_modlist_blacklist = getList("player_modlist_blacklist.list", new ArrayList<>());
        server_modlist_whitelist_enable = getBoolean("server_modlist_whitelist.enable", false);
        server_modlist_whitelist = getString("server_modlist_whitelist.list", ServerAPI.modlists_All.toString().replace(", mohist", ""));
        maxBees = getInt("max-bees-in-hive", 3);
        bookAnimationTick = getBoolean("enchantment-table-book-animation-tick", false);
        networkmanager_debug = getBoolean("mohist.networkmanager.debug", false);
        hideJoinModsList = getBoolean("forge.hidejoinmodslist", false);
        networkmanager_intercept = getList("mohist.networkmanager.intercept", new ArrayList<>());
        check_update_auto_download = getBoolean("mohist.check_update_auto_download", false);
    }
}