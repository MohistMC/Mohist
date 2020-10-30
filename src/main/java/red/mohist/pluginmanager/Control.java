package red.mohist.pluginmanager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

public class Control {

    public static PluginDescriptionFile getDescription(File file) throws InvalidDescriptionException, IOException {
        JarFile jar = new JarFile(file);
        ZipEntry zip = jar.getEntry("plugin.yml");
        if (zip == null) {
            jar.close();
            return null;
        }
        PluginDescriptionFile pdf = new PluginDescriptionFile(jar.getInputStream(zip));
        jar.close();
        return pdf;
    }

    public static File getFile(JavaPlugin plugin) {
        Field file;

        try {
            file = JavaPlugin.class.getDeclaredField("file");
            file.setAccessible(true);
            return (File) file.get(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void enablePlugin(Plugin plugin) {
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    public static void disablePlugin(Plugin plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    public static Plugin loadPlugin(File plugin) {
        Plugin p;
        try {
            p = Bukkit.getPluginManager().loadPlugin(plugin);
            try {
                p.onLoad();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;
        } catch (InvalidPluginException e) {
            e.printStackTrace();
        } catch (InvalidDescriptionException e) {
            e.printStackTrace();
        } catch (UnknownDependencyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean unloadPlugin(Plugin plugin, Boolean reloaddependents) {
        SimpleCommandMap commandMap;
        PluginManager pluginManager = Bukkit.getPluginManager();

        String pName = plugin.getName();
        List<Plugin> plugins;
        Map<String, Plugin> names;
        Map<String, Command> commands;
        ArrayList<Plugin> reload = new ArrayList<Plugin>();
        disablePlugin(plugin);
        if (reloaddependents) {
            for (Plugin p : pluginManager.getPlugins()) {
                List<String> depend = p.getDescription().getDepend();
                if (depend != null) {
                    for (String s : depend) {
                        if (s.equals(pName)) {
                            if (!reload.contains(p)) {
                                reload.add(p);
                                unloadPlugin(p, false);
                            }
                        }
                    }
                }

                List<String> softDepend = p.getDescription().getSoftDepend();
                if (softDepend != null) {
                    for (String s : softDepend) {
                        if (s.equals(pName)) {
                            if (!reload.contains(p)) {
                                reload.add(p);
                                unloadPlugin(p, false);
                            }
                        }
                    }
                }
            }
        }
        for (Plugin p : reload) {
            Bukkit.getServer().broadcastMessage(p.getName() + "\n");
        }
        try {
            Field pluginsField, lookupNamesField, commandMapField, knownCommandsField;

            pluginsField = pluginManager.getClass().getDeclaredField("plugins");
            lookupNamesField = pluginManager.getClass().getDeclaredField("lookupNames");
            commandMapField = pluginManager.getClass().getDeclaredField("commandMap");
            knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");

            pluginsField.setAccessible(true);
            lookupNamesField.setAccessible(true);
            commandMapField.setAccessible(true);
            knownCommandsField.setAccessible(true);

            plugins = (List<Plugin>) pluginsField.get(pluginManager);
            names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);
            commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
            commands = (Map<String, Command>) knownCommandsField.get(commandMap);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        if (commandMap != null) {
            synchronized (commandMap) {
                Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Command> entry = it.next();
                    if (entry.getValue() instanceof PluginCommand) {
                        PluginCommand c = (PluginCommand) entry.getValue();
                        if (c.getPlugin() == plugin) {
                            c.unregister(commandMap);
                            it.remove();
                        }
                    }
                }
            }
        }


        synchronized (pluginManager) {
            if (plugins != null && plugins.contains(plugin)) {
                plugins.remove(plugin);
            }

            if (names != null && names.containsKey(pName)) {
                names.remove(pName);
            }
        }

        JavaPluginLoader jpl = (JavaPluginLoader) plugin.getPluginLoader();
        Field loaders = null;

        try {
            loaders = jpl.getClass().getDeclaredField("loaders");
            loaders.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CopyOnWriteArrayList<String> loaderMap = (CopyOnWriteArrayList<String>) loaders.get(jpl);

            loaderMap.remove(plugin.getDescription().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassLoader cl = plugin.getClass().getClassLoader();

        try {
            ((URLClassLoader) cl).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.gc();

        if (reloaddependents) {
            for (Plugin value : reload) {
                enablePlugin(loadPlugin(getFile((JavaPlugin) value)));
            }
        }

        return true;
    }

}
