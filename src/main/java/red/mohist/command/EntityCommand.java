package red.mohist.command;

import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import red.mohist.util.i18n.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EntityCommand extends Command {

    private final List<String> params = Arrays.asList("reload", "dump-existing");

    public EntityCommand(String name) {
        super(name);
        this.description = "Entity tick limiting commands";
        this.usageMessage = "/entity [reload|dump-existing]";
        this.setPermission("mohist.command.entity");
    }

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

        return list;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isOp() || !testPermission(sender)) {
            sender.sendMessage(Message.getString("command.nopermission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "reload":
                MinecraftServer.entityConfig.load();
                sender.sendMessage(ChatColor.GREEN + "entity.yml reload complete.");
                break;

            case "dump-existing":
                MinecraftServer.entityConfig.save();
                sender.sendMessage(ChatColor.GREEN + "entity.yml updated with found Entities");
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }
}