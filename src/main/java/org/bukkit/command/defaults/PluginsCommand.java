package org.bukkit.command.defaults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import red.mohist.util.i18n.Message;
import red.mohist.util.pluginmanager.PluginManagers;

public class PluginsCommand extends BukkitCommand {
    public PluginsCommand(@NotNull String name) {
        super(name);
        this.description = "Gets a list of plugins running on the server";
        this.usageMessage = "/plugins";
        this.setPermission("bukkit.command.plugins");
        this.setAliases(Arrays.asList("pl"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, @NotNull String[] args) {
        if (!testPermission(sender)) {
            sender.sendMessage(Message.getString("command.nopermission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Plugins " + getPluginList());
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "load":
                PluginManagers.loadPluginCommand(sender, currentAlias, args);
                break;
            case "unload":
                PluginManagers.unloadPluginCommand(sender, currentAlias, args);
                break;
            case "reload":
                PluginManagers.reloadPluginCommand(sender, currentAlias, args);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }
        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    @NotNull
    private String getPluginList() {
        StringBuilder pluginList = new StringBuilder();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

        for (Plugin plugin : plugins) {
            if (pluginList.length() > 0) {
                pluginList.append(ChatColor.WHITE);
                pluginList.append(", ");
            }

            pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
            pluginList.append(plugin.getDescription().getName());

            if (plugin.getDescription().getProvides().size() > 0) {
                pluginList.append(" (").append(String.join(", ", plugin.getDescription().getProvides())).append(")");
            }
        }

        return "(" + plugins.length + "): " + pluginList.toString();
    }
}
