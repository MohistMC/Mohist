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
import red.mohist.MohistThreadCost;
import red.mohist.api.PlayerAPI;
import red.mohist.api.ServerAPI;
import red.mohist.util.i18n.Message;

public class MohistCommand extends Command {

    public MohistCommand(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [mods|playermods|printthreadcost|lang|item]";
    }

    private List<String> params = Arrays.asList("mods", "playermods", "printthreadcost", "lang", "item");

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
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
            case "printthreadcost":
                MohistThreadCost.dumpThreadCpuTime();
                break;
            case "lang":
                sender.sendMessage(ChatColor.GREEN +ServerAPI.getLanguage());
                break;
            case "item":
                if (args.length > 0 && "info".equals(args[1].toLowerCase(Locale.ENGLISH))){
                    ItemCommand.info(sender);
                }
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }
}
