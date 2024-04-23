package com.mohistmc.mohist.plugins.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpacceptCommands extends Command {

    public TpacceptCommands(String name) {
        super(name);
        this.usageMessage = "/tpaccept";
        this.setPermission("mohist.command.tpa");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (TpaComamands.tpa.containsKey(player)) {
                final Player a = TpaComamands.tpa.get(player);
                a.teleport(player);
                player.sendMessage("You've accepted the request");
                a.sendMessage("The request has been accepted!");
                TpaComamands.tpa.remove(player);
            }
            else {
                sender.sendMessage("The request no longer exists!");
            }
        }
        return false;
    }
}
