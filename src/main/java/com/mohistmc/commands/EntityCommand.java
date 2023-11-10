package com.mohistmc.commands;

import com.mohistmc.api.ChatComponentAPI;
import com.mohistmc.api.EntityAPI;
import com.mohistmc.util.I18n;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/27 20:03:27
 */
public class EntityCommand extends Command {

    private final List<String> params = Arrays.asList("info", "remove");
    public EntityCommand(@NotNull String name) {
        super(name);
        this.description = "View entity info";
        this.usageMessage = "/entity [info|remove]";
        this.setPermission("mohist.command.entity");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + I18n.as("error.notplayer"));
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "info" -> {
                Entity entity = player.getTargetEntity(3);
                if (entity != null) {
                    ChatComponentAPI.sendMessageByCopy(player, ChatColor.GRAY + "Type: ", entity.getType().name());
                    ChatComponentAPI.sendMessageByCopy(player, ChatColor.GRAY + "UUID: ", entity.getUniqueId().toString());
                    ChatComponentAPI.sendMessageByCopy(player, ChatColor.GRAY + "NBT(CraftBukkit) - ", EntityAPI.getNBTAsString(entity));
                    return true;
                }
            }
            case "remove" -> {
                Entity entity = player.getTargetEntity(3);
                if (entity != null) entity.remove();
                return true;
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
        }

        return false;
    }
}
