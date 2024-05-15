package com.mohistmc.mohist.plugins.back;

import com.mohistmc.mohist.util.I18n;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class BackCommands extends Command {

    public BackCommands(String name) {
        super(name);
        this.usageMessage = "/back";
        this.setPermission("mohist.command.back");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (BackConfig.INSTANCE.has(player.getName())) {
               player.teleport(BackConfig.INSTANCE.getLocation(player));
               player.sendMessage(I18n.as("backcommands.success",
                       BackConfig.INSTANCE.getBackType(player).isTeleport() ? I18n.as("backcommands.backtype.teleport") : I18n.as("backcommands.backtype.death")));
            }
            else {
                sender.sendMessage(I18n.as("backcommands.none"));
            }
        }
        return false;
    }

    public static void hookTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) return;
        BackConfig.INSTANCE.saveLocation(event.getPlayer(), event.getFrom(), BackType.TELEPORT);
    }

    public static void hooktDeath(PlayerDeathEvent event) {
        BackConfig.INSTANCE.saveLocation(event.getEntity(), event.getEntity().getLocation(), BackType.DEATH);
    }
}
