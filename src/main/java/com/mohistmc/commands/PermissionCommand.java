package com.mohistmc.commands;

import com.mohistmc.MohistMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        this.description = MohistMC.i18n.get("permissioncmd.description");
        this.usageMessage = "/permission <check>";
        this.setPermission("mohist.command.permission");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "check" -> {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /permission check <player> <permission>");
                    return false;
                }
                String permission = args[2];
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    sender.sendMessage(player.hasPermission(permission) ? ChatColor.GREEN + "true" : ChatColor.RED + "false");
                } else {
                    sender.sendMessage(ChatColor.RED + MohistMC.i18n.get("mohistcmd.playermods.playernotOnlinep1") + args[1] + MohistMC.i18n.get("mohistcmd.playermods.playernotOnlinep2") );
                }
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
        }
        return false;
    }

    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
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
