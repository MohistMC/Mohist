package org.spigotmc;

import java.io.File;

import com.mohistmc.util.i18n.i18n;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SpigotCommand extends Command {

    public SpigotCommand(String name) {
        super(name);
        this.description = "Spigot related commands";
        this.usageMessage = "/spigot reload";
        this.setPermission("bukkit.command.spigot");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + i18n.get("spigotcommand.4") + " "+ usageMessage);
            return false;
        }

        if (args[0].equals("reload")) {
            Command.broadcastCommandMessage(sender, ChatColor.RED + i18n.get("spigotcommand.1"));
            Command.broadcastCommandMessage(sender, ChatColor.RED + i18n.get("spigotcommand.2"));

            MinecraftServer console = MinecraftServer.getServer();
            SpigotConfig.init((File) console.options.valueOf("spigot-settings"));
            for (ServerWorld world : console.getAllLevels()) {
                world.spigotConfig.init();
            }
            console.server.reloadCount++;

            Command.broadcastCommandMessage(sender, ChatColor.GREEN + i18n.get("spigotcommand.3"));
        }

        return true;
    }
}
