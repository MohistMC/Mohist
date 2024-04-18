package com.mohistmc.plugins.tpa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpadenyCommands extends Command {

    public TpadenyCommands(String name) {
        super(name);
        this.usageMessage = "/tpadeny";
        this.setPermission("mohist.command.tpa");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (TpaComamands.tpa.containsKey(player)) {
                final Player a = TpaComamands.tpa.get(player);
                a.sendMessage("You've rejected the other party's delivery request!");
                player.sendMessage("Your teleportation request has been denied!");
                TpaComamands.tpa.remove(player);
            }
            else {
                sender.sendMessage("The request no longer exists!");
            }
        }
        return false;
    }


}
