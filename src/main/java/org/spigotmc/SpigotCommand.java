package org.spigotmc;

import com.mohistmc.MohistMC;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;

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
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        if (args[0].equals("reload")) {
            Command.broadcastCommandMessage(sender, ChatColor.RED + MohistMC.i18n.as("mohist.i18n.64"));
            Command.broadcastCommandMessage(sender, ChatColor.RED + MohistMC.i18n.as("mohist.i18n.65"));

            MinecraftServer console = MinecraftServer.getServer();
            SpigotConfig.init((File) MinecraftServer.options.valueOf("spigot-settings"));
            for (ServerLevel world : console.getAllLevels()) {
                world.spigotConfig.init();
            }
            console.server.reloadCount++;

            Command.broadcastCommandMessage(sender, ChatColor.GREEN + MohistMC.i18n.as("mohist.i18n.66"));
        }

        return true;
    }
}
