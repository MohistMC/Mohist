package com.mohistmc.plugins.tpa;

import com.mohistmc.MohistConfig;
import com.mohistmc.util.I18n;
import java.util.HashMap;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaComamands extends Command {

    public static HashMap<Player, Player> tpa = new HashMap<>();

    public TpaComamands(String name) {
        super(name);
        this.usageMessage = "/tpa <player_name>";
        if (MohistConfig.tpa_permissions_enable) {
            this.setPermission("mohist.command.tpa");
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (sender instanceof Player player) {
            Player a = Bukkit.getPlayer(args[0]);
            if (a == null) {
                player.sendMessage(I18n.as("tpacommands.noplayer"));
                return false;
            }
            if (a == player) {
                player.sendMessage(I18n.as("tpacommands.yourself"));
                return false;
            }
            TextComponent accept = new TextComponent("§f/tpaccept");
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(I18n.as("tpacommands.hover.accept"))));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
            TextComponent deny = new TextComponent("§f/tpadeny");
            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(I18n.as("tpacommands.hover.reject"))));
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny"));
            a.sendMessage(I18n.as("tpacommands.sent", player.getName()));
            a.spigot().sendMessage(accept);
            a.spigot().sendMessage(deny);
            player.sendMessage(I18n.as("tpacommands.successfully"));
            tpa.remove(a);
            tpa.put(a, player);
            return true;
        }
        return false;
    }
}
