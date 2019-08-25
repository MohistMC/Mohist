package red.mohist;

import com.google.common.base.Throwables;
import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.command.defaultcomamnd.ItemCommand;
import red.mohist.command.defaultcomamnd.MohistCommand;
import red.mohist.command.defaultcomamnd.VersionCommand;
import red.mohist.i18n.Message;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MohistConfig {

    private static final String HEADER = "This is the main configuration file for Mohist.\n"
            + "You can change \"update: \n  version: Stable or Debug to get universal version or debug version\"\n";
    private static final Pattern SPACE = Pattern.compile(" ");
    private static final Pattern NOT_NUMERIC = Pattern.compile("[^-\\d.]");
    public static String unknownCommandMessage = Message.getString(Message.Use_Unkonw_Comamnd);
    public static String outdatedClientMessage = Message.getString(Message.outdated_Client);
    public static String outdatedServerMessage = Message.getString(Message.outdated_Server);
    public static String server_type = "FML";
    public static String lang = "en_US";
    public static boolean support_nocmd = false;
    /**
     * 开启时,控制台会不停地输入所有线程的耗时
     */
    public static boolean printThreadTimeCost = false;
    /**
     * 是否导出经过remap的插件类
     */
    public static boolean dumpRemapPluginClass = false;
    /**
     * 打印remap插件
     */
    public static boolean printRemapPluginClass = false;
    /**
     * 是否打印无效的remap规则
     */
    public static boolean printInvalidMapping = false;
    /**
     * 是否启用插件多版本兼容
     */
    public static boolean multiVersionRemap = true;
    /**
     * 启用多版本兼容的插件
     */
    public static Set<String> multiVersionRemapPlugins = new HashSet<>();
    /**
     * 是否启用nmsRemap
     */
    public static boolean nmsRemap = true;
    /**
     * 是否启用反射remap
     */
    public static boolean reflectRemap = true;
    /**
     * 非玩家实体碰撞其他实体的间隔时间
     */
    public static int entityCollideFrequency = 2;
    /**
     * 一个tick内,一个实体可以碰撞或被碰撞多少次
     */
    public static int maxEntityCollisionsPerTick = 8;
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    /*========================================================================*/
    static Map<String, Command> commands;
    private static File CONFIG_FILE;

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException ex) {
        } catch (InvalidConfigurationException ex) {
            Mohist.LOGGER.error("Could not load mohist.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header(HEADER);
        config.options().copyDefaults(true);

        commands = new HashMap<String, Command>();
        commands.put("mohist", new MohistCommand("mohist"));
        commands.put("item", new ItemCommand("item"));
        commands.put("version", new VersionCommand("version"));

        version = getInt("config-version", 1);
        set("config-version", 1);
        if (version < 0) {
            config.options().header(HEADER);
            set("messages.use-unknow-command", unknownCommandMessage);
            set("messages.Outdate-Client", outdatedClientMessage);
            set("messages.Outdate-Server", outdatedServerMessage);
            set("update.version", "Stable");
            set("update.autoget", false);
        }
        unknownCommandMessage = transform(getString("messages.use-unknow-command", unknownCommandMessage));
        outdatedClientMessage = transform(getString("messages.Outdate-Client", outdatedClientMessage));
        outdatedServerMessage = transform(getString("messages.Outdate-Server", outdatedServerMessage));
        entityCollideFrequency = getInt("perfomance.entityCollideFrequency", entityCollideFrequency);
        maxEntityCollisionsPerTick = getInt("perfomance.maxEntityCollisionsPerTick", maxEntityCollisionsPerTick);
        dumpRemapPluginClass = getBoolean("remap.dumpRemapPluginClass", dumpRemapPluginClass);
        printRemapPluginClass = getBoolean("remap.printRemapPluginClass", printRemapPluginClass);
        printThreadTimeCost = getBoolean("debug.printThreadTimeCost", printThreadTimeCost);
        printInvalidMapping = getBoolean("debug.printInvalidMapping", printInvalidMapping);
        multiVersionRemap = getBoolean("remap.multiVersionRemap", multiVersionRemap);

        List<String> list = getList("remap.multiVersionRemapPlugins", new ArrayList<>());
        if (list != null) {
            multiVersionRemapPlugins.addAll(list);
        }
        reflectRemap = getBoolean("remap.reflectRemap", reflectRemap);
        nmsRemap = getBoolean("remap.nmsRemap", nmsRemap);
        server_type = getString("server-type", server_type);
        lang = getString("lang", lang);
        support_nocmd = getBoolean("support_nocmd", support_nocmd);
        readConfig(MohistConfig.class, null);
    }

    private static String transform(String s) {
        return ChatColor.translateAlternateColorCodes('&', s).replaceAll("\\\\n", "\n");
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServerInst().server.getCommandMap().register(entry.getKey(), "Mohist", entry.getValue());
        }
        new Metrics();
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
                        Mohist.LOGGER.error("Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Mohist.LOGGER.error("Could not save " + CONFIG_FILE, ex);
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
            time = TimeUnit.SECONDS.toDays(seconds) + "d";
            seconds %= 60 * 60 * 24;
        }

        if (seconds > 60 * 60) {
            time = TimeUnit.SECONDS.toHours(seconds) + "h";
            seconds %= 60 * 60;
        }

        if (seconds > 0) {
            time = TimeUnit.SECONDS.toMinutes(seconds) + "m";
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

    public static <T> T getValue(String path, Class<T> valueType, T def) {
        Object val = config.get(path);
        if (valueType.isInstance(val)) {
            return (T) val;
        }
        config.set(path, def);
        return def;
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

    public static void reload() {
        try {
            config.save(CONFIG_FILE);
            config.load(CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Reload read
        unknownCommandMessage = transform(getString("messages.use-unknow-command", unknownCommandMessage));
        outdatedClientMessage = transform(getString("messages.Outdate-Client", outdatedClientMessage));
        outdatedServerMessage = transform(getString("messages.Outdate-Server", outdatedServerMessage));
    }
}
