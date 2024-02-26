package com.mohistmc.commands;

import com.mohistmc.MohistConfig;
import com.mohistmc.util.I18n;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/5 16:24:49
 */
public class PingCommand extends Command {

    public PingCommand(String name) {
        super(name);
        this.setPermission("mohist.command.ping");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && (sender.isOp() || testPermission(sender))) {
            for (Player param : Bukkit.getOnlinePlayers()) {
                if (param.getName().startsWith(args[0])) {
                    list.add(param.getName());
                }
            }
        }

        return list;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }

        if (args.length == 0) {
            if (sender instanceof Player player) {
                String output = String.format(MohistConfig.pingCommandOutput, player.getName(), player.getPing());
                sender.sendMessage(output);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + I18n.as("error.notplayer"));
                return false;
            }
        } else if(args.length == 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player != null) {
                String output = String.format(MohistConfig.pingCommandOutput, player.getName(), player.getPing());
                sender.sendMessage(output);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + I18n.as("mohistcmd.playermods.playernotOnline", args[0]));
                return false;
            }
        }

        return false;
    }
}
