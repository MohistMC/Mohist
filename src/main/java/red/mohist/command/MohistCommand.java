package red.mohist.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import red.mohist.api.PlayerAPI;
import red.mohist.api.ServerAPI;
import red.mohist.util.i18n.Message;

public class MohistCommand extends Command {

    public MohistCommand(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [mods|playermods|lang]";
    }

    private List<String> params = Arrays.asList("mods", "playermods", "lang");

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<String>();
        if (args.length == 1 && sender.isOp()) {
            for (String param : params) {
                if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                    list.add(param);
                }
            }
        }

        return list;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Message.getString("command.nopermission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if ("mods".equals(args[0].toLowerCase(Locale.ENGLISH))) {// Not recommended for use in games, only test output
            sender.sendMessage(ChatColor.GREEN + "" + ServerAPI.getModSize() + " " + ServerAPI.getModList());
        } else if ("playermods".equals(args[0].toLowerCase(Locale.ENGLISH))) {// Not recommended for use in games, only test output
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
        } else if ("lang".equals(args[0].toLowerCase(Locale.ENGLISH))) {
            sender.sendMessage(ChatColor.GREEN + ServerAPI.getLanguage());
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        return true;
    }
}
