package com.mohistmc.plugins.tpa;

import com.mohistmc.MohistConfig;
import com.mohistmc.util.I18n;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpadenyCommands extends Command {

    public TpadenyCommands(String name) {
        super(name);
        this.usageMessage = "/tpadeny";
        if (MohistConfig.tpa_permissions_enable) {
            this.setPermission("mohist.command.tpa");
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (TpaComamands.tpa.containsKey(player)) {
                final Player a = TpaComamands.tpa.get(player);
                a.sendMessage(I18n.as("tpadenycommands.successfully.you"));
                player.sendMessage(I18n.as("tpadenycommands.successfully.me"));
                TpaComamands.tpa.remove(player);
            }
            else {
                sender.sendMessage(I18n.as("tpadenycommands.nokey"));
            }
        }
        return false;
    }


}
