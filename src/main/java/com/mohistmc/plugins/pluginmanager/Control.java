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
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.SimplePluginManager;

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
            MohistMC.LOGGER.error(ioe);
        }

        return null;
    }

    public static Plugin loadPlugin(File plugin) {
        try {
            Plugin p = Bukkit.getPluginManager().loadPlugin(plugin);
            p.onLoad();
            return p;
        } catch (Exception e) {
            MohistMC.LOGGER.error(e);
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
            if (!plugin1.equals(plugin)) {
                continue;
            }

            manager.disablePlugin(plugin);
            plugins.remove(plugin);
            lookupNames.remove(plugin.getDescription().getName());

            Iterator<Map.Entry<String, Command>> it = knownCommands.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Command> entry = it.next();
                if (!(entry.getValue() instanceof PluginCommand command)) {
                    continue;
                }
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
