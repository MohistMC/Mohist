package com.mohistmc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 5:33:43
 */
public class BansCommand extends Command {

    private final List<String> params = Arrays.asList("add");
    public BansCommand(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/bans [add] [item|entity]";
        this.setPermission("mohist.command.bans");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to perform this command.");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "add" -> {
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /bans add [item|entity]");
                    return false;
                }
                if (args[1].equals("item")) {
                    Inventory inventory = Bukkit.createInventory(player, 54, "§4Add bans item");
                    player.openInventory(inventory);
                } else if (args[1].equals("entity")) {
                    Inventory inventory = Bukkit.createInventory(player, 54, "§4Add bans entity");
                    player.openInventory(inventory);
                }else {
                    sender.sendMessage(ChatColor.RED + "Usage: /bans add [item|entity]");
                    return false;
                }
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
        }
        return false;
    }

    @Override
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
