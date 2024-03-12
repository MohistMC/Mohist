/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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

import com.mohistmc.util.I18n;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class PluginManagers {

    public static String permission = "mohist.command.plugin";

    public static boolean loadPluginCommand(CommandSender sender, String label, String[] split) {
        if (split.length < 2) {
            sender.sendMessage(I18n.as("pluginscommand.load", label));
            return false;
        }
        String pluginName = split[1];
        String jarName = pluginName + (pluginName.endsWith(".jar") ? "" : ".jar");
        File toLoad = new File("plugins" + File.separator + jarName);

        if (!toLoad.exists()) {
            jarName = pluginName + (pluginName.endsWith(".jar") ? ".unloaded" : ".jar.unloaded");
            toLoad = new File("plugins" + File.separator + jarName);
            if (!toLoad.exists()) {
                sender.sendMessage(I18n.as("pluginscommand.nofile", pluginName));
                return false;
            } else {
                String fileName = jarName.substring(0, jarName.length() - (".unloaded".length()));
                toLoad = new File("plugins" + File.separator + fileName);
                File unloaded = new File("plugins" + File.separator + jarName);
                unloaded.renameTo(toLoad);
            }
        }

        PluginDescriptionFile desc = Control.getDescription(toLoad);
        if (desc == null) {
            sender.sendMessage(I18n.as("pluginscommand.noyml", pluginName));
            return false;
        }

        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (desc.getName().equals(p.getName())) {
                sender.sendMessage(I18n.as("pluginscommand.alreadyloaded", desc.getName()));
                return true;
            }
        }
        Plugin p = Control.loadPlugin(toLoad);
        if (p != null) {
            Bukkit.getServer().getPluginManager().enablePlugin(p);
            sender.sendMessage(I18n.as("pluginscommand.loaded", p.getDescription().getName(), p.getDescription().getVersion()));
        } else {
            sender.sendMessage(I18n.as("pluginscommand.notload", pluginName));
            return false;
        }

        return true;
    }

    public static boolean unloadPluginCommand(CommandSender sender, String label, String[] split) {
        if (split.length < 2) {
            sender.sendMessage(I18n.as("pluginscommand.unload", label));
            return true;
        }

        String pluginName = split[1];
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin(pluginName);

        if (p == null) {
            sender.sendMessage(I18n.as("pluginscommand.noplugin", pluginName));
            return false;
        } else {
            if (Control.unloadPlugin(p)) {
                sender.sendMessage(I18n.as("pluginscommand.unloaded", p.getDescription().getName(), p.getDescription().getVersion()));
            } else {
                sender.sendMessage(I18n.as("pluginscommand.notunload", pluginName));
                return false;
            }
        }
        return true;
    }

    public static boolean reloadPluginCommand(CommandSender sender, String label, String[] split) {

        if (split.length < 2) {
            sender.sendMessage(I18n.as("pluginscommand.reload", label));
            return false;
        }
        String pluginName = split[1];
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin(pluginName);

        if (p == null) {
            sender.sendMessage(I18n.as("pluginscommand.noplugin", pluginName));
            return false;
        } else {
            File file = ((JavaPlugin) p).getFile();

            if (file == null) {
                sender.sendMessage(I18n.as("pluginscommand.nojar", p.getName()));
                return false;
            }

            File name = new File("plugins" + File.separator + file.getName());
            JavaPlugin loaded = null;
            if (!Control.unloadPlugin(p)) {
                sender.sendMessage(I18n.as("pluginscommand.unloaderror", pluginName));
                return false;
            } else if ((loaded = (JavaPlugin) Control.loadPlugin(name)) == null) {
                sender.sendMessage(I18n.as("pluginscommand.nojar", pluginName));
                return false;
            }

            Bukkit.getPluginManager().enablePlugin(loaded);
            sender.sendMessage(I18n.as("pluginscommand.reloaded", pluginName, loaded.getDescription().getVersion()));
        }
        return true;
    }
}
