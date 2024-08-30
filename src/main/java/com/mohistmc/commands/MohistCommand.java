/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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

package com.mohistmc.commands;

import com.mohistmc.MohistMC;
import com.mohistmc.api.PlayerAPI;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.util.I18n;
import com.mohistmc.util.MohistThreadCost;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MohistCommand extends Command {

    private final List<String> params = Arrays.asList("mods", "playermods", "reload", "version", "channels_incom", "channels_outgo", "speed", "printthreadcost");

    public MohistCommand(String name) {
        super(name);
        this.description = "Mohist related commands";
        this.usageMessage = "/mohist [mods|playermods|reload|version|channels_incom|channels_outgo|speed]";
        this.setPermission("mohist.command.mohist");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if ((sender.isOp() || testPermission(sender))) {
            if (args.length == 1) {
                for (String param : params) {
                    if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                        list.add(param);
                    }
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("playermods")) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            }
        }


        return list;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
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
                sender.sendMessage(ChatColor.GREEN + I18n.as("mohistcmd.insidemods") + ServerAPI.modlists_Inside.size() + ") -> " + ServerAPI.modlists_Inside);
                sender.sendMessage(ChatColor.GREEN + I18n.as("mohistcmd.clientOnlymods")+ ServerAPI.modlists_Client.size() + ") -> " + ServerAPI.modlists_Client);
                sender.sendMessage(ChatColor.GREEN + I18n.as("mohistcmd.serverOnlymods") + ServerAPI.modlists_Server.size() + ") -> " + ServerAPI.modlists_Server);
                sender.sendMessage(ChatColor.GREEN + I18n.as("mohistcmd.allMods") + ServerAPI.modlists_All.size() + ") -> " + ServerAPI.modlists_All);
            }
            case "playermods" -> {
                // Not recommended for use in games, only test output
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mohist playermods <playername>");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    sender.sendMessage(ChatColor.GREEN + String.valueOf(PlayerAPI.getModSize(player)) + " " + PlayerAPI.getModlist(player).toString());
                } else {
                    sender.sendMessage(ChatColor.RED + I18n.as("mohistcmd.playermods.playernotOnline", args[1]));
                }
            }
            case "reload" -> {
                Command.broadcastCommandMessage(sender, ChatColor.RED + I18n.as("mohistcmd.reload.line1"));
                Command.broadcastCommandMessage(sender, ChatColor.RED + I18n.as("mohistcmd.reload.line2"));

                com.mohistmc.MohistConfig.init((File) MinecraftServer.options.valueOf("mohist-settings"));

                MinecraftServer.getServer().server.reloadCount++;
                sender.sendMessage(ChatColor.GREEN + "mohist-config/mohist.yml directory reload complete.");
            }
            case "version" -> {
                sender.sendMessage("Mohist: " + MohistMC.versionInfo.mohist());
                sender.sendMessage("Forge: " + MohistMC.versionInfo.forge());
                sender.sendMessage("NeoForge: " + MohistMC.versionInfo.neoforge());
                sender.sendMessage("Bukkit: " + MohistMC.versionInfo.bukkit());
                sender.sendMessage("CraftBukkit: " + MohistMC.versionInfo.craftbukkit());
                sender.sendMessage("Spigot: " + MohistMC.versionInfo.spigot());
            }
            case "channels_incom" -> sender.sendMessage(ServerAPI.channels_Incoming().toString());
            case "printthreadcost" -> MohistThreadCost.dumpThreadCpuTime();
            case "channels_outgo" -> sender.sendMessage(ServerAPI.channels_Outgoing().toString());
            case "speed" -> {
                if (sender instanceof Player p) {
                    if (args.length == 2 && p.isOp()) {
                        if (this.isFloat(args[1])) {
                            if (p.isFlying()) {
                                float speed = Float.parseFloat(args[1]);
                                if (speed >= 0.0f && speed < 11.0f) {
                                    p.setFlySpeed(speed / 10.0f);
                                    p.sendMessage(I18n.as("mohistcmd.playerflightspeedSet") + speed);
                                }
                            } else {
                                float speed = Float.parseFloat(args[1]);
                                if (speed >= 0.0f && speed < 11.0f) {
                                    p.setWalkSpeed(speed / 10.0f);
                                    p.sendMessage(I18n.as("mohistcmd.playerwalkspeedset") + speed);
                                }
                            }
                        }
                        if (args[0].equalsIgnoreCase("reset")) {
                            p.setFlySpeed(0.1f);
                            p.setWalkSpeed(0.2f);
                            p.sendMessage(I18n.as("mohistcmd.flightAndWalkspeedRestore"));
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + I18n.as("error.notplayer"));
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
