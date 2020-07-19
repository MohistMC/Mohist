package red.mohist.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

import static red.mohist.util.HasteUtils.paste;

public class GetPluginListCommand extends Command {
    private static String sendToHaste = "";

    public GetPluginListCommand(String name) {
        super(name);
        this.description = "Paste the list of your plugins on hastebin and get the link.";
        this.usageMessage = "/getpluginlist";
        this.setPermission("mohist.command.getpluginlist");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (testPermission(sender) || sender.isOp()) {
            for (Plugin p : Bukkit.getServer().getPluginManager().getPlugins())
                sendToHaste = sendToHaste + "\nName : " + p.getName() + "\nVersion : " + p.getDescription().getVersion() + "\n---------";
            try {
                sender.sendMessage("Link of the list of your plugins : " + paste(sendToHaste));
            } catch (IOException e) {
                System.out.println("Unable to paste the list of your plugins.");
            }
        }
        return true;
    }
}
