package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraftforge.common.ForgeVersion;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;
import red.mohist.Mohist;
import red.mohist.util.i18n.Message;

public class VersionCommand extends Command {
    public VersionCommand(String name) {
        super(name);

        this.description = "Gets the version of this server including any plugins in use";
        this.usageMessage = "/version [plugin name]";
        this.setAliases(Collections.singletonList("ver"));
        this.setPermission("bukkit.command.version");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            sender.sendMessage(Message.getString("command.nopermission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("This server is running " + Bukkit.getName() + " version " + Mohist.getVersion() +  " (MC: 1.12.2) (Implementing API version " + Bukkit.getBukkitVersion() + ", Forge version " + ForgeVersion.getVersion() + ")");
        } else {
            StringBuilder name = new StringBuilder();

            for (String arg : args) {
                if (name.length() > 0) {
                    name.append(' ');
                }

                name.append(arg);
            }

            String pluginName = name.toString();
            Plugin exactPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (exactPlugin != null) {
                describeToSender(exactPlugin, sender);
                return true;
            }

            boolean found = false;
            pluginName = pluginName.toLowerCase(java.util.Locale.ENGLISH);
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                if (plugin.getName().toLowerCase(java.util.Locale.ENGLISH).contains(pluginName)) {
                    describeToSender(plugin, sender);
                    found = true;
                }
            }

            if (!found) {
                sender.sendMessage("This server is not running any plugin by that name.");
                sender.sendMessage("Use /plugins to get a list of plugins.");
            }
        }
        return true;
    }

    private void describeToSender(Plugin plugin, CommandSender sender) {
        PluginDescriptionFile desc = plugin.getDescription();
        sender.sendMessage(ChatColor.GREEN + desc.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + desc.getVersion());

        if (desc.getDescription() != null) {
            sender.sendMessage(desc.getDescription());
        }

        if (desc.getWebsite() != null) {
            sender.sendMessage("Website: " + ChatColor.GREEN + desc.getWebsite());
        }

        if (!desc.getAuthors().isEmpty()) {
            if (desc.getAuthors().size() == 1) {
                sender.sendMessage("Author: " + getAuthors(desc));
            } else {
                sender.sendMessage("Authors: " + getAuthors(desc));
            }
        }
    }

    private String getAuthors(final PluginDescriptionFile desc) {
        StringBuilder result = new StringBuilder();
        List<String> authors = desc.getAuthors();

        for (int i = 0; i < authors.size(); i++) {
            if (result.length() > 0) {
                result.append(ChatColor.WHITE);

                if (i < authors.size() - 1) {
                    result.append(", ");
                } else {
                    result.append(" and ");
                }
            }

            result.append(ChatColor.GREEN);
            result.append(authors.get(i));
        }

        return result.toString();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1 && sender.isOp()) {
            List<String> completions = new ArrayList<>();
            String toComplete = args[0].toLowerCase(java.util.Locale.ENGLISH);
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                if (StringUtil.startsWithIgnoreCase(plugin.getName(), toComplete)) {
                    completions.add(plugin.getName());
                }
            }
            return completions;
        }
        return ImmutableList.of();
    }
}