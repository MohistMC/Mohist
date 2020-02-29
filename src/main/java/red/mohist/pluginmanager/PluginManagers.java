package red.mohist.pluginmanager;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginManagers {

    public static boolean hasPermission(CommandSender sender) {
        if (sender != Bukkit.getServer().getConsoleSender()) {
            if (sender.isOp()) {
                return true;
            }

            sender.sendMessage(Message.getString("command.nopermission"));
            return false;
        }
        return true;
    }

    public static boolean loadPluginCommand(CommandSender sender, String label, String[] split) {
        if (!hasPermission(sender)) {
            return true;
        }

        if (split.length < 2) {
            Object[] f = {label};
            sender.sendMessage(Message.getFormatString("pluginscommand.load", f));
            return true;
        }
        String jarName = split[1] + (split[1].endsWith(".jar") ? "" : ".jar");
        File toLoad = new File("plugins" + File.separator + jarName);

        if (!toLoad.exists()) {
            jarName = split[1] + (split[1].endsWith(".jar") ? ".unloaded" : ".jar.unloaded");
            toLoad = new File("plugins" + File.separator + jarName);
            if (!toLoad.exists()) {
                Object[] f = {split[1]};
                sender.sendMessage(Message.getFormatString("pluginscommand.nofile", f));
                return true;
            } else {
                String fileName = jarName.substring(0, jarName.length() - (".unloaded".length()));
                toLoad = new File("plugins" + File.separator + fileName);
                File unloaded = new File("plugins" + File.separator + jarName);
                unloaded.renameTo(toLoad);
            }
        }

        PluginDescriptionFile desc = Control.getDescription(toLoad);
        if (desc == null) {
            Object[] f = {split[1]};
            sender.sendMessage(Message.getFormatString("pluginscommand.noyml", f));
            return true;
        }
        Plugin[] pl = Bukkit.getPluginManager().getPlugins();
        ArrayList<Plugin> plugins = new ArrayList<>(java.util.Arrays.asList(pl));
        for (Plugin p : plugins) {
            if (desc.getName().equals(p.getName())) {
                Object[] f = {desc.getName()};
                sender.sendMessage(Message.getFormatString("pluginscommand.alreadyloaded", f));
                return true;
            }
        }
        Plugin p = null;
        if ((p = Control.loadPlugin(toLoad)) != null) {
            Control.enablePlugin(p);
            Object[] d = {p.getDescription().getName(), p.getDescription().getVersion()};
            sender.sendMessage(Message.getFormatString("pluginscommand.loaded", d));
        } else {
            Object[] d = {split[1]};
            sender.sendMessage(Message.getFormatString("pluginscommand.notload", d));
        }

        return true;
    }

    public static boolean unloadPluginCommand(CommandSender sender, String label, String[] split) {
        if (!hasPermission(sender)) {
            return true;
        }

        if (split.length < 2) {
            Object[] f = {label};
            sender.sendMessage(Message.getFormatString("pluginscommand.unload", f));
            return true;
        }

        Plugin p = Bukkit.getServer().getPluginManager().getPlugin(split[1]);

        if (p == null) {
            Object[] f = {split[1]};
            sender.sendMessage(Message.getFormatString("pluginscommand.noplugin", f));
        } else {
            if (Control.unloadPlugin(p, true)) {
                Object[] d = {p.getDescription().getName(), p.getDescription().getVersion()};
                sender.sendMessage(Message.getFormatString("pluginscommand.unloaded", d));
            } else {
                Object[] d = {split[1]};
                sender.sendMessage(Message.getFormatString("pluginscommand.notunload", d));
            }
        }

        return true;
    }

    public static boolean reloadPluginCommand(CommandSender sender, String label, String[] split) {
        if (!hasPermission(sender)) {
            return true;
        }

        if (split.length < 2) {
            Object[] f = {label};
            sender.sendMessage(Message.getFormatString("pluginscommand.reload", f));
            return true;
        }

        Plugin p = Bukkit.getServer().getPluginManager().getPlugin(split[1]);

        if (p == null) {
            Object[] f = {split[1]};
            sender.sendMessage(Message.getFormatString("pluginscommand.noplugin", f));
        } else {
            File file = Control.getFile((JavaPlugin) p);

            if (file == null) {
                Object[] f = {p.getName()};
                sender.sendMessage(Message.getFormatString("pluginscommand.nojar", f));
                return true;
            }

            File name = new File("plugins" + File.separator + file.getName());
            JavaPlugin loaded = null;
            if (!Control.unloadPlugin(p, false)) {
                Object[] f = {split[1]};
                sender.sendMessage(Message.getFormatString("pluginscommand.unloaderror", f));
            } else if ((loaded = (JavaPlugin) Control.loadPlugin(name)) == null) {
                Object[] f = {split[1]};
                sender.sendMessage(Message.getFormatString("pluginscommand.nojar", f));
            }

            Control.enablePlugin(loaded);
            Object[] d = {split[1], loaded.getDescription().getVersion()};
            sender.sendMessage(Message.getFormatString("pluginscommand.reloaded", d));
        }
        return true;
    }
    
    private static Map<String, List<String>> plugins = new HashMap<String, List<String>>() {{
        /* 表示exampl插件的1.0版本和2.0版本有这个漏洞 */
        /* Indicates that the 1.0 and 2.0 versions of the example plugin have this vulnerability */
        /*
        put("example", new ArrayList<String>() {{
            add("1.0");
            add("2.0");
        }});// 格式：插件名称,有漏洞的版本

         */
        /* 表示exampl1插件的所有版本都有这个漏洞 */
        /* Indicates that all versions of the exampl1 plugin have this vulnerability */
        /*
        put("example1", new ArrayList<String>() {{
            add("all");
        }});
         */

        put("example", new ArrayList<String>() {{
            add("all");
        }});
        put("example1", new ArrayList<String>() {{
            add("1.0");
        }});
        put("example2", new ArrayList<String>() {{
            add("1.0");
            add("2.0");
        }});
    }};

    public static synchronized boolean checkBug(Plugin plugin, String version) {
        // 判断plugins是否为空，是的话返回false,否则判断plugins是否包含插件名称且plugins获取到的List是否包含当前插件版本，如果version为null，返回是否包含all，如果不为null,返回是否包含version
        return !plugins.isEmpty() ? (plugins.containsKey(plugin.getName()) && (plugins.get(plugin.getName()).contains("all") || plugins.get(plugin.getName()).contains(version))) : false;
    }
}
