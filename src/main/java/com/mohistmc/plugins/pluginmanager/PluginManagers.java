/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.plugins.pluginmanager;

import com.mohistmc.MohistMC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

import static com.mohistmc.MohistMC.i18n;

public class PluginManagers {

    public static String permission = "mohist.command.plugin";

    public static boolean loadPluginCommand(CommandSender sender, String label, String... split) {
        if (split.length < 2) {
            Object[] f = {label};
            sender.sendMessage(i18n.get("pluginscommand.load", f));
            return true;
        }
        String pluginName = split[1];
        Object[] objects = {pluginName};
        String jarName = pluginName + (pluginName.endsWith(".jar") ? "" : ".jar");
        File toLoad = new File("plugins", jarName);;

        if (!toLoad.exists()) {
            jarName = split[1] + (split[1].endsWith(".jar") ? ".unloaded" : ".jar.unloaded");
            toLoad = new File("plugins" + File.separator + jarName);
            if (!toLoad.exists()) {
                sender.sendMessage(i18n.get("pluginscommand.nofile", objects));
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
            sender.sendMessage(i18n.get("pluginscommand.noyml", objects));
            return true;
        }
        Plugin[] pl = Bukkit.getPluginManager().getPlugins();
        ArrayList<Plugin> plugins = new ArrayList<>(java.util.Arrays.asList(pl));
        for (Plugin p : plugins) {
            if (desc.getName().equals(p.getName())) {
                sender.sendMessage(i18n.get("pluginscommand.alreadyloaded", new Object[]{desc.getName()}));
                return true;
            }
        }
        Plugin p = Control.loadPlugin(toLoad);
        if (p != null) {
            Bukkit.getServer().getPluginManager().enablePlugin(p);
            sender.sendMessage(i18n.get("pluginscommand.loaded", new Object[]{p.getDescription().getName(), p.getDescription().getVersion()}));
        } else {
            sender.sendMessage(i18n.get("pluginscommand.notload", objects));
        }

        return true;
    }

    public static boolean unloadPluginCommand(CommandSender sender, String label, String[] split) {
        if (split.length < 2) {
            sender.sendMessage(i18n.get("pluginscommand.unload", new Object[]{label}));
            return true;
        }

        Plugin p = Bukkit.getServer().getPluginManager().getPlugin(split[1]);
        Object[] objects = new Object[]{split[1]};

        if (p == null) {
            sender.sendMessage(i18n.get("pluginscommand.noplugin", objects));
        } else {
            if (Control.unloadPlugin(p)) {
                sender.sendMessage(i18n.get("pluginscommand.unloaded", new Object[]{p.getDescription().getName(), p.getDescription().getVersion()}));
            } else {
                sender.sendMessage(i18n.get("pluginscommand.notunload", objects));
            }
        }

        return true;
    }

    public static boolean reloadPluginCommand(CommandSender sender, String label, String[] split) {

        if (split.length < 2) {
            sender.sendMessage(i18n.get("pluginscommand.reload", new Object[]{label}));
            return true;
        }

        Plugin p = Bukkit.getServer().getPluginManager().getPlugin(split[1]);
        Object[] objects = new Object[]{split[1]};

        if (p == null) {
            sender.sendMessage(i18n.get("pluginscommand.noplugin", objects));
        } else {
            File file = ((JavaPlugin) p).getFile();

            if (file == null) {
                sender.sendMessage(i18n.get("pluginscommand.nojar", new Object[]{p.getName()}));
                return true;
            }

            File name = new File("plugins" + File.separator + file.getName());
            JavaPlugin loaded = null;
            if (!Control.unloadPlugin(p)) {
                sender.sendMessage(i18n.get("pluginscommand.unloaderror", objects));
            } else if ((loaded = (JavaPlugin) Control.loadPlugin(name)) == null) {
                sender.sendMessage(i18n.get("pluginscommand.nojar", objects));
            }

            Bukkit.getPluginManager().enablePlugin(loaded);
            sender.sendMessage(i18n.get("pluginscommand.reloaded", new Object[]{split[1], loaded.getDescription().getVersion()}));
        }
        return true;
    }
}
