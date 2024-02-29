package com.mohistmc.commands;

import com.mohistmc.util.I18n;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 *
 * This is useful when no permission management plugin is installed
 *
 * @date 2023/7/20 15:19:28
 */
public class PermissionCommand extends Command {

    private final List<String> params = List.of("check");

    public PermissionCommand(String name) {
        super(name);
        this.description = I18n.as("permissioncmd.description");
        this.usageMessage = "/permission <check>";
        this.setPermission("mohist.command.permission");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if (args[0].toLowerCase(Locale.ENGLISH).equals("check")) {
            if (args.length != 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /permission check <player> <permission>");
                return false;
            }
            String permission = args[2];
            Player player = Bukkit.getPlayer(args[1]);
            if (player != null) {
                sender.sendMessage(player.hasPermission(permission) ? ChatColor.GREEN + "true" : ChatColor.RED + "false");
            } else {
                sender.sendMessage(ChatColor.RED + I18n.as("mohistcmd.playermods.playernotOnline", args[1]));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        return false;
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && (sender.isOp() || testPermission(sender))) {
            for (String param : params) {
                if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                    list.add(param);
                }
            }
        }

        return list;
    }

}
