package com.mohistmc.util.pluginmanager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Control {

    public static PluginDescriptionFile getDescription(File file) {
        try {
            JarFile jar = new JarFile(file);
            ZipEntry zip = jar.getEntry("plugin.yml");
            if (zip == null) {
                jar.close();
                return null;
            }
            PluginDescriptionFile pdf = new PluginDescriptionFile(jar.getInputStream(zip));
            jar.close();
            return pdf;
        } catch (InvalidDescriptionException | IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
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

    public static Plugin loadPlugin(File plugin) {
        try {
            Plugin p = Bukkit.getPluginManager().loadPlugin(plugin);
            p.onLoad();
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean unloadPlugin(Plugin plugin) {
        SimplePluginManager manager = (SimplePluginManager) Bukkit.getServer().getPluginManager();

        List<Plugin> plugins = ObfuscationReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "plugins");
        Map<String, Plugin> lookupNames = ObfuscationReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "lookupNames");
        SimpleCommandMap commandMap = ObfuscationReflectionHelper.getPrivateValue(SimplePluginManager.class, manager, "commandMap");
        Map<String, Command> knownCommands = ObfuscationReflectionHelper.getPrivateValue(SimpleCommandMap.class, commandMap, "knownCommands");

        for (Plugin plugin1 : manager.getPlugins()) {
            if (!plugin1.equals(plugin)) continue;

            manager.disablePlugin(plugin);
            plugins.remove(plugin);
            lookupNames.remove(plugin.getDescription().getName());

            Iterator<Map.Entry<String, Command>> it = knownCommands.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Command> entry = (Map.Entry) it.next();
                if (!(entry.getValue() instanceof PluginCommand)) continue;
                PluginCommand command = (PluginCommand) entry.getValue();
                if (command.getPlugin() == plugin) {
                    command.unregister(commandMap);
                    it.remove();
                }
            }
            return true;
        }

        return false;
    }

}
