package com.mohistmc.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BiomeCommand extends Command {

    public BiomeCommand(@NotNull String name) {
        super(name);
        this.description = "biome tool!";
        this.usageMessage = "/biome [info]";
        this.setPermission("mohist.command.biome");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) return false;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
            if (args[0].toLowerCase().equals("info")) {
                player.sendMessage(player.getLocation().getBlock().getBiome().name());
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to perform this command.");
            return false;
        }
        return false;
    }
}
