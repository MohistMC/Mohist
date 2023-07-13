package com.mohistmc;

import com.google.common.base.Throwables;
import com.mohistmc.commands.BackupWorldCommand;
import com.mohistmc.commands.DumpCommand;
import com.mohistmc.commands.GetPluginListCommand;
import com.mohistmc.commands.ItemsCommand;
import com.mohistmc.commands.MohistCommand;
import com.mohistmc.commands.PluginCommand;
import com.mohistmc.plugins.MohistPlugin;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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

        MohistPlugin.registerCommands(commands);

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
        config.addDefault( path, def );
        return (List<T>) config.getList( path, config.getList( path ) );
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

    private static void mohist() {
        show_logo = getBoolean("mohist.show_logo", true);
        mohist_lang = getString("mohist.lang", "xx_XX");
        mohist_vanilla_lang = getString("mohist.vanilla_lang", "en_us");
        check_update = getBoolean("mohist.check_update", true);
        maximumRepairCost = getInt("anvilfix.maximumrepaircost", 40);
        enchantment_fix = getBoolean("anvilfix.enchantment_fix", false);
        max_enchantment_level = getInt("anvilfix.max_enchantment_level", 32767);
        player_modlist_blacklist_enable = getBoolean("player_modlist_blacklist.enable", false);
        player_modlist_blacklist = getList("player_modlist_blacklist.list", Collections.emptyList());
    }
}