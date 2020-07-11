package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import red.mohist.util.i18n.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends BukkitCommand {
    public ReloadCommand(String name) {
        super(name);
        this.description = Message.getString("reloadcommand.des");
        this.usageMessage = "/reload";
        this.setPermission("bukkit.command.reload");
        this.setAliases(Arrays.asList("rl"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        Command.broadcastCommandMessage(sender, ChatColor.RED + Message.getString("reloadcommand.execute0"));
        Command.broadcastCommandMessage(sender, ChatColor.RED + Message.getString("reloadcommand.execute1"));
        Bukkit.reload();
        Command.broadcastCommandMessage(sender, ChatColor.GREEN + Message.getString("reloadcommand.execute2"));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }
}
