package com.mohistmc.command;

import com.mohistmc.api.PlayerAPI;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.configuration.MohistConfig;
import com.mohistmc.configuration.TickConfig;
import com.mohistmc.util.i18n.i18n;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MohistCommand extends Command {

    public MohistCommand(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [mods|playermods|printthreadcost|lang|item|reload|give]";
        this.setPermission("mohist.command.mohist");
    }

    private List<String> params = Arrays.asList("mods", "playermods", "printthreadcost", "lang", "item", "reload", "give");

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && (sender.isOp() || testPermission(sender))) {
            for (String param : params) {
                if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                    list.add(param);
                }
            }
        }
        if (args.length >= 3 && args[0].equals("give")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
        }

        if (args.length == 2 && args[0].equals("item")) {
            list.add("info");
        }

        return list;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "mods":
                // Not recommended for use in games, only test output
                sender.sendMessage(ChatColor.GREEN + "" + ServerAPI.getModSize() + " " + ServerAPI.getModList());
                break;
            case "playermods":
                // Not recommended for use in games, only test output
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    sender.sendMessage(ChatColor.GREEN + "" + PlayerAPI.getModSize(player) + " " + PlayerAPI.getModlist(player));
                } else {
                    sender.sendMessage(ChatColor.RED + "The player [" + args[1] + "] is not online.");
                }
                break;
            case "lang":
                sender.sendMessage(ChatColor.GREEN + i18n.getLocale());
                break;
            case "item":
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mohist item info");
                    return false;
                }
                if ("info".equals(args[1].toLowerCase(Locale.ENGLISH))){
                    ItemCommand.info(sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /mohist item info");
                }
                break;
            case "reload":
                if (MohistConfig.instance != null)
                    MohistConfig.instance.load();
                TickConfig.ENTITIES.reloadConfig();
                TickConfig.TILES.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "mohist-config directory reload complete.");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }



        return true;
    }
}
