package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import red.mohist.util.i18n.Message;
import red.mohist.util.pluginmanager.PluginManagers;

import java.util.*;

public class PluginsCommand extends BukkitCommand {
    private final List<String> params = Arrays.asList("load", "unload", "reload");

    public PluginsCommand(String name) {
        super(name);
        this.description = Message.getString("pluginscommand.des");
        this.usageMessage = "/plugins [load|unload|reload] [name]";
        this.setAliases(Collections.singletonList("pl"));
        this.setPermission("bukkit.command.plugins");
    }

    private boolean checkparam(String args) {
        //TODO
        for (String param : params) {
            if (args.equalsIgnoreCase(param)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
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

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> tabs = new ArrayList<>();
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

    private String getPluginList() {
        // Paper start
        TreeMap<String, ChatColor> plugins = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            plugins.put(plugin.getDescription().getName(), plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
        }

        StringBuilder pluginList = new StringBuilder();
        for (Map.Entry<String, ChatColor> entry : plugins.entrySet()) {
            if (pluginList.length() > 0) {
                pluginList.append(ChatColor.WHITE);
                pluginList.append(", ");
            }

            pluginList.append(entry.getValue());
            pluginList.append(entry.getKey());
        }

        return "(" + plugins.size() + "): " + pluginList.toString();
        // Paper end
    }
}
