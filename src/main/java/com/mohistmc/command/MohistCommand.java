/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.command;

import com.mohistmc.api.PlayerAPI;
import com.mohistmc.api.ServerAPI;
import net.minecraft.DetectedVersion;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MohistCommand extends Command {

    private final List<String> params = Arrays.asList("mods", "playermods", "printthreadcost", "lang", "item", "reload", "version", "channels", "speed");

    public MohistCommand(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [mods|playermods|printthreadcost|lang|item|reload|version]";
        this.setPermission("mohist.command.mohist");
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

        if (args.length == 2 && args[0].equals("item")) {
            list.add("info");
        }

        return list;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "mods" -> {
                // Not recommended for use in games, only test output
                sender.sendMessage(ChatColor.GREEN + "Inside mods: (" + ServerAPI.modlists_Inside.size() + ") -> " + ServerAPI.modlists_Inside);
                sender.sendMessage(ChatColor.GREEN + "Only Client mods: (" + ServerAPI.modlists_Client.size() + ") -> " + ServerAPI.modlists_Client);
                sender.sendMessage(ChatColor.GREEN + "Only Server mods: (" + ServerAPI.modlists_Server.size() + ") -> " + ServerAPI.modlists_Server);
                sender.sendMessage(ChatColor.GREEN + "All mods: (" + ServerAPI.modlists_All.size() + ") -> " + ServerAPI.modlists_All);
            }
            case "playermods" -> {
                // Not recommended for use in games, only test output
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mohist playermods <playername>");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    sender.sendMessage(ChatColor.GREEN + String.valueOf(PlayerAPI.getModSize(player)) + " " + PlayerAPI.getModlist(player));
                } else {
                    sender.sendMessage(ChatColor.RED + "The player [" + args[1] + "] is not online.");
                }
            }
            case "item" -> {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mohist item info");
                    return false;
                }
                if ("info".equals(args[1].toLowerCase(Locale.ENGLISH))) {
                    ItemCommand.info(sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /mohist item info");
                }
            }
            case "reload" -> {
                Command.broadcastCommandMessage(sender, ChatColor.RED + "Please note that this command is not supported and may cause issues.");
                Command.broadcastCommandMessage(sender, ChatColor.RED + "If you encounter any issues please use the /stop command to restart your server.");

                com.mohistmc.MohistConfig.init((File) MinecraftServer.options.valueOf("mohist-settings"));

                MinecraftServer.getServer().server.reloadCount++;
                sender.sendMessage(ChatColor.GREEN + "mohist-config/mohist.yml directory reload complete.");
            }
            case "version" -> {
                sender.sendMessage("Mohist: " + DetectedVersion.BUILT_IN.getName());
                sender.sendMessage("Forge: " + ForgeVersion.getVersion());
                String[] cbs = CraftServer.class.getPackage().getImplementationVersion().split("-");
                sender.sendMessage("Bukkit: " + cbs[0]);
                sender.sendMessage("CraftBukkit: " + cbs[1]);
                sender.sendMessage("Spigot: " + cbs[2]);
            }
            case "channels" -> sender.sendMessage(ServerAPI.channels.toString());
            case "speed" -> {
                if (sender instanceof Player p) {
                    if (args.length == 2 && p.isOp()) {
                        if (this.isFloat(args[1])) {
                            if (p.isFlying()) {
                                float speed = Float.parseFloat(args[1]);
                                if (speed >= 0.0f && speed < 11.0f) {
                                    p.setFlySpeed(speed / 10.0f);
                                    p.sendMessage("飞行速度已设置为 §b" + speed);
                                }
                            } else {
                                float speed = Float.parseFloat(args[1]);
                                if (speed >= 0.0f && speed < 11.0f) {
                                    p.setWalkSpeed(speed / 10.0f);
                                    p.sendMessage("行走速度已设置为 §b" + speed);
                                }
                            }
                        }
                        if (args[0].equalsIgnoreCase("reset")) {
                            p.setFlySpeed(0.1f);
                            p.setWalkSpeed(0.2f);
                            p.sendMessage("行走和飞行速度已恢复默认.");
                        }
                    }
                } else {
                    sender.sendMessage("§c控制台无法超速行驶");
                }
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
        }


        return true;
    }

    private boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
