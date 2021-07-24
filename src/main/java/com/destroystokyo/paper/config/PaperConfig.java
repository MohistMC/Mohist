package com.destroystokyo.paper.config;

import co.aikar.timings.Timings;
import co.aikar.timings.TimingsManager;
import com.destroystokyo.paper.PaperCommand;
import com.google.common.base.Throwables;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Pattern;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class PaperConfig {

    private static File CONFIG_FILE;
    private static final String HEADER = "This is the main configuration file for Paper.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to Paper,\n"
            + "join us in our Discord or IRC channel.\n"
            + "\n"
            + "Discord: https://discord.gg/papermc\n"
            + "IRC: #paper @ irc.esper.net ( https://webchat.esper.net/?channels=paper ) \n"
            + "Website: https://papermc.io/ \n"
            + "Docs: https://paper.readthedocs.org/ \n";
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    private static boolean verbose;
    private static boolean fatalError;
    /*========================================================================*/

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException ex) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load paper.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header(HEADER);
        config.options().copyDefaults(true);
        verbose = getBoolean("verbose", false);

        commands = new HashMap<String, Command>();
        commands.put("paper", new PaperCommand("paper"));

        version = getInt("config-version", 20);
        set("config-version", 20);
        readConfig(PaperConfig.class, null);
    }

    protected static void logError(String s) {
        Bukkit.getLogger().severe(s);
    }

    protected static void fatal(String s) {
        fatalError = true;
        throw new RuntimeException("Fatal paper.yml config error: " + s);
    }

    protected static void log(String s) {
        if (verbose) {
            Bukkit.getLogger().info(s);
        }
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Paper", entry.getValue());
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
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

    private static final Pattern SPACE = Pattern.compile(" ");
    private static final Pattern NOT_NUMERIC = Pattern.compile("[^-\\d.]");

    public static int getSeconds(String str) {
        str = SPACE.matcher(str).replaceAll("");
        final char unit = str.charAt(str.length() - 1);
        str = NOT_NUMERIC.matcher(str).replaceAll("");
        double num;
        try {
            num = Double.parseDouble(str);
        } catch (Exception e) {
            num = 0D;
        }
        switch (unit) {
            case 'd':
                num *= (double) 60 * 60 * 24;
                break;
            case 'h':
                num *= (double) 60 * 60;
                break;
            case 'm':
                num *= (double) 60;
                break;
            default:
            case 's':
                break;
        }
        return (int) num;
    }

    protected static String timeSummary(int seconds) {
        String time = "";

        if (seconds > 60 * 60 * 24) {
            time += TimeUnit.SECONDS.toDays(seconds) + "d";
            seconds %= 60 * 60 * 24;
        }

        if (seconds > 60 * 60) {
            time += TimeUnit.SECONDS.toHours(seconds) + "h";
            seconds %= 60 * 60;
        }

        if (seconds > 0) {
            time += TimeUnit.SECONDS.toMinutes(seconds) + "m";
        }
        return time;
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static float getFloat(String path, float def) {
        // TODO: Figure out why getFloat() always returns the default value.
        return (float) getDouble(path, (double) def);
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

    public static String noPermissionMessage = "&cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";

    private static void noPermissionMessage() {
        noPermissionMessage = ChatColor.translateAlternateColorCodes('&', getString("messages.no-permission", noPermissionMessage));
    }

    public static boolean useDisplayNameInQuit = false;

    private static void useDisplayNameInQuit() {
        useDisplayNameInQuit = getBoolean("use-display-name-in-quit-message", useDisplayNameInQuit);
    }

    public static boolean asyncChunks = false;
    private static void asyncChunks() {
        ConfigurationSection section;
        if (version < 15) {
            section = config.createSection("settings.async-chunks");
            section.set("threads", -1);
        } else {
            section = config.getConfigurationSection("settings.async-chunks");
            if (section == null) {
                section = config.createSection("settings.async-chunks");
            }
        }
        // Clean up old configs
        if (section.contains("load-threads")) {
            if (!section.contains("threads")) {
                section.set("threads", section.get("load-threads"));
            }
            section.set("load-threads", null);
        }
        section.set("generation", null);
        section.set("enabled", null);
        section.set("thread-per-world-generation", null);

        int threads = getInt("settings.async-chunks.threads", -1);
        int cpus = Runtime.getRuntime().availableProcessors();
        if (threads <= 0) {
            threads = (int) Math.min(Integer.getInteger("paper.maxChunkThreads", 8), Math.max(1, cpus - 1));
        }
        if (cpus == 1 && !Boolean.getBoolean("Paper.allowAsyncChunksSingleCore")) {
            asyncChunks = false;
        } else {
            asyncChunks = true;
        }

        // Let Shared Host set some limits
        String sharedHostThreads = System.getenv("PAPER_ASYNC_CHUNKS_SHARED_HOST_THREADS");
        if (sharedHostThreads != null) {
            try {
                threads = Math.max(1, Math.min(threads, Integer.parseInt(sharedHostThreads)));
            } catch (NumberFormatException ignored) {
            }
        }

        if (!asyncChunks) {
            log("Async Chunks: Disabled - Chunks will be managed synchronously, and will cause tremendous lag.");
        } else {
            log("Async Chunks: Enabled - Chunks will be loaded much faster, without lag.");
        }
    }

    public static boolean useOptimizedTickList = true;

    private static void useOptimizedTickList() {
        if (config.contains("settings.use-optimized-ticklist")) { // don't add default, hopefully temporary config
            useOptimizedTickList = config.getBoolean("settings.use-optimized-ticklist");
        }
    }

    public static String timingsServerName;
    private static void timings() {
        final boolean timings = getBoolean("timings.enabled", true);
        final boolean verboseTimings = getBoolean("timings.verbose", true);
        TimingsManager.privacy = getBoolean("timings.server-name-privacy", false);
        TimingsManager.hiddenConfigs = (List<String>)getList("timings.hidden-config-entries", Lists.newArrayList("database", "settings.bungeecord-addresses", "settings.velocity-support.secret"));
        if (!TimingsManager.hiddenConfigs.contains("settings.velocity-support.secret")) {
            TimingsManager.hiddenConfigs.add("settings.velocity-support.secret");
        }
        final int timingHistoryInterval = getInt("timings.history-interval", 300);
        final int timingHistoryLength = getInt("timings.history-length", 3600);
        PaperConfig.timingsServerName = getString("timings.server-name", "Unknown Server");
        Timings.setVerboseTimingsEnabled(verboseTimings);
        Timings.setTimingsEnabled(timings);
        Timings.setHistoryInterval(timingHistoryInterval * 20);
        Timings.setHistoryLength(timingHistoryLength * 20);
        log("Timings: " + timings + " - Verbose: " + verboseTimings + " - Interval: " + timeSummary(Timings.getHistoryInterval() / 20) + " - Length: " + timeSummary(Timings.getHistoryLength() / 20) + " - Server Name: " + PaperConfig.timingsServerName);
    }

}
