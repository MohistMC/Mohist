package org.bukkit.command.defaults;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import com.mohistmc.util.i18n.Message;

public class PluginsCommand extends BukkitCommand {
    public PluginsCommand(String name) {
        super(name);
        this.description = Message.getString("pluginscommand.des");
        this.usageMessage = "/plugins";
        this.setPermission("bukkit.command.plugins");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length == 0) {
            sender.sendMessage(Message.getString("pluginscommand.1") + " " + getPluginList());
            return false;
        }
        return true;
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
