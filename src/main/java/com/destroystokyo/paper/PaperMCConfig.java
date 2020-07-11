package com.destroystokyo.paper;

import co.aikar.timings.Timings;
import co.aikar.timings.TimingsManager;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

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

public class PaperMCConfig {

    private static final String HEADER = "This is the main configuration file for Paper.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to Paper,\n"
            + "join us in our IRC channel.\n"
            + "\n"
            + "IRC: #paper @ irc.spi.gt ( http://irc.spi.gt/iris/?channels=paper )\n"
            + "Wiki: https://paper.readthedocs.org/ \n"
            + "Paper Forums: https://aquifermc.org/ \n";
    private static final Pattern SPACE = Pattern.compile(" ");
    private static final Pattern NOT_NUMERIC = Pattern.compile("[^-\\d.]");
    /*========================================================================*/
    public static YamlConfiguration config;
    public static int minChunkLoadThreads = 2;
    public static boolean enableFileIOThreadSleep;
    public static boolean loadPermsBeforePlugins = true;
    public static int regionFileCacheSize = 256;
    public static boolean enablePlayerCollisions = true;
    public static boolean saveEmptyScoreboardTeams = false;
    public static boolean bungeeOnlineMode = true;
    public static int packetInSpamThreshold = 300;
    public static String flyingKickPlayerMessage = "Flying is not enabled on this server";
    public static String flyingKickVehicleMessage = "Flying is not enabled on this server";
    public static int playerAutoSaveRate = -1;
    public static int maxPlayerAutoSavePerTick = 10;
    public static boolean removeInvalidStatistics = false;
    public static boolean suggestPlayersWhenNullTabCompletions = true;
    public static String authenticationServersDownKickMessage = ""; // empty = use translatable message
    public static boolean savePlayerData = true;
    public static boolean useAlternativeLuckFormula = false;
    public static int tabSpamIncrement = 10;
    public static int tabSpamLimit = 500;
    public static int maxBookPageSize = 2560;
    public static double maxBookTotalSizeMultiplier = 0.98D;
    public static boolean allowPistonDuplication;
    static int version;
    static Map<String, Command> commands;
    private static File CONFIG_FILE;
    private static boolean verbose;
    /*========================================================================*/
    private static boolean metricsStarted;

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

        commands = new HashMap<>();

        version = getInt("config-version", 13);
        set("config-version", 13);
        readConfig(PaperMCConfig.class, null);
    }

    protected static void logError(String s) {
        Bukkit.getLogger().severe(s);
    }

    protected static void log(String s) {
        if (verbose) {
            Bukkit.getLogger().info(s);
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
                num *= 60;
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
        return (float) getDouble(path, def);
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

    private static void timings() {
        boolean timings = getBoolean("timings.enabled", true);
        boolean verboseTimings = getBoolean("timings.verbose", true);
        TimingsManager.privacy = getBoolean("timings.server-name-privacy", false);
        TimingsManager.hiddenConfigs = getList("timings.hidden-config-entries", Lists.newArrayList("database", "settings.bungeecord-addresses"));
        int timingHistoryInterval = getInt("timings.history-interval", 300);
        int timingHistoryLength = getInt("timings.history-length", 3600);


        Timings.setVerboseTimingsEnabled(verboseTimings);
        Timings.setTimingsEnabled(timings);
        Timings.setHistoryInterval(timingHistoryInterval * 20);
        Timings.setHistoryLength(timingHistoryLength * 20);

        log("Timings: " + timings +
                " - Verbose: " + verboseTimings +
                " - Interval: " + timeSummary(Timings.getHistoryInterval() / 20) +
                " - Length: " + timeSummary(Timings.getHistoryLength() / 20));
    }

    private static void chunkLoadThreads() {
        minChunkLoadThreads = Math.min(6, getInt("settings.min-chunk-load-threads", 2)); // Keep people from doing stupid things with max of 6
    }

    private static void enableFileIOThreadSleep() {
        enableFileIOThreadSleep = getBoolean("settings.sleep-between-chunk-saves", false);
        if (enableFileIOThreadSleep) {
            Bukkit.getLogger().info("Enabled sleeping between chunk saves, beware of memory issues");
        }
    }

    private static void loadPermsBeforePlugins() {
        loadPermsBeforePlugins = getBoolean("settings.load-permissions-yml-before-plugins", true);
    }

    private static void regionFileCacheSize() {
        regionFileCacheSize = getInt("settings.region-file-cache-size", 256);
    }

    private static void enablePlayerCollisions() {
        enablePlayerCollisions = getBoolean("settings.enable-player-collisions", true);
    }

    private static void saveEmptyScoreboardTeams() {
        saveEmptyScoreboardTeams = getBoolean("settings.save-empty-scoreboard-teams", false);
    }

    private static void bungeeOnlineMode() {
        bungeeOnlineMode = getBoolean("settings.bungee-online-mode", true);
    }

    private static void packetInSpamThreshold() {
        if (version < 11) {
            int oldValue = getInt("settings.play-in-use-item-spam-threshold", 300);
            set("settings.incoming-packet-spam-threshold", oldValue);
        }
        packetInSpamThreshold = getInt("settings.incoming-packet-spam-threshold", 300);
    }

    private static void flyingKickMessages() {
        flyingKickPlayerMessage = getString("messages.kick.flying-player", flyingKickPlayerMessage);
        flyingKickVehicleMessage = getString("messages.kick.flying-vehicle", flyingKickVehicleMessage);
    }

    private static void playerAutoSaveRate() {
        playerAutoSaveRate = getInt("settings.player-auto-save-rate", -1);
        maxPlayerAutoSavePerTick = getInt("settings.max-player-auto-save-per-tick", -1);
        if (maxPlayerAutoSavePerTick == -1) { // -1 Automatic / "Recommended"
            // 10 should be safe for everyone unless your mass spamming player auto save
            maxPlayerAutoSavePerTick = (playerAutoSaveRate == -1 || playerAutoSaveRate > 100) ? 10 : 20;
        }
    }

    private static void removeInvalidStatistics() {
        if (version < 12) {
            boolean oldValue = getBoolean("remove-invalid-statistics", false);
            set("settings.remove-invalid-statistics", oldValue);
        }
        removeInvalidStatistics = getBoolean("settings.remove-invalid-statistics", false);
    }

    private static void suggestPlayersWhenNull() {
        suggestPlayersWhenNullTabCompletions = getBoolean("settings.suggest-player-names-when-null-tab-completions", suggestPlayersWhenNullTabCompletions);
    }

    private static void authenticationServersDownKickMessage() {
        authenticationServersDownKickMessage = Strings.emptyToNull(getString("messages.kick.authentication-servers-down", authenticationServersDownKickMessage));
    }

    private static void savePlayerData() {
        savePlayerData = getBoolean("settings.save-player-data", savePlayerData);
        if (!savePlayerData) {
            Bukkit.getLogger().log(Level.WARNING, "Player Data Saving is currently disabled. Any changes to your players data, " +
                    "such as inventories, experience points, advancements and the like will not be saved when they log out.");
        }
    }

    private static void useAlternativeLuckFormula() {
        useAlternativeLuckFormula = getBoolean("settings.use-alternative-luck-formula", false);
        if (useAlternativeLuckFormula) {
            Bukkit.getLogger().log(Level.INFO, "Using Aikar's Alternative Luck Formula to apply Luck attribute to all loot pool calculations. See https://luckformula.emc.gs");
        }
    }

    private static void tabSpamLimiters() {
        tabSpamIncrement = getInt("settings.spam-limiter.tab-spam-increment", tabSpamIncrement);
        tabSpamLimit = getInt("settings.spam-limiter.tab-spam-limit", tabSpamLimit);
    }

    private static void maxBookSize() {
        maxBookPageSize = getInt("settings.book-size.page-max", maxBookPageSize);
        maxBookTotalSizeMultiplier = getDouble("settings.book-size.total-multiplier", maxBookTotalSizeMultiplier);
        if (maxBookPageSize == 1024 && maxBookTotalSizeMultiplier == 0.90D) {
            config.set("settings.book-size.page-max", 2560);
            config.set("settings.book-size.total-multiplier", 0.98D);
            maxBookPageSize = 2560;
            maxBookTotalSizeMultiplier = 0.98D;
        }
    }

    private static void allowPistonDuplication() {
        allowPistonDuplication = getBoolean("settings.unsupported-settings.allow-piston-duplication", config.getBoolean("settings.unsupported-settings.allow-tnt-duplication", false));
        set("settings.unsupported-settings.allow-tnt-duplication", null);
    }
}
