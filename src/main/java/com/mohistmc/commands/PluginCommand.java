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

package com.mohistmc.commands;

import com.mohistmc.plugins.pluginmanager.PluginManagers;
import com.mohistmc.util.I18n;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PluginCommand extends Command {

    private final List<String> params = Arrays.asList("load", "unload", "reload");

    public PluginCommand(String name) {
        super(name);
        this.description = I18n.as("plugincmd.description");
        this.usageMessage = "/plugin [load|unload|reload] [name]";
        this.setPermission("mohist.command.plugin");
    }

    private boolean checkparam(String args) {
        for (String param : params) {
            if (args.equalsIgnoreCase(param)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "load" -> PluginManagers.loadPluginCommand(sender, commandLabel, args);
            case "unload" -> PluginManagers.unloadPluginCommand(sender, commandLabel, args);
            case "reload" -> PluginManagers.reloadPluginCommand(sender, commandLabel, args);
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1 && (sender.isOp() || testPermission(sender))) {
            for (String param : params) {
                if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                    tabs.add(param);
                }
            }
        }
        if (args.length == 2 && (sender.isOp() || testPermission(sender))) {
            if (checkparam(args[0])) {
                for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
                    String plname = pl.getDescription().getName();
                    if (plname.toLowerCase().startsWith(args[1].toLowerCase())) {
                        tabs.add(plname);
                    }
                }
            }
        }
        return tabs;
    }
}