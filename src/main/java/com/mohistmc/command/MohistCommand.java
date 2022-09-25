/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
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
import com.mohistmc.configuration.MohistConfig;
import com.mohistmc.configuration.TickConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.SharedConstants;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.entity.Player;

public class MohistCommand extends Command {

    private List<String> params = Arrays.asList("mods", "playermods", "printthreadcost", "lang", "item", "reload", "version", "channels");

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
                    sender.sendMessage(ChatColor.RED + "Usage: /mohist playermods <playername>");
                    return false;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    sender.sendMessage(ChatColor.GREEN + "" + PlayerAPI.getModSize(player) + " " + PlayerAPI.getModlist(player));
                } else {
                    sender.sendMessage(ChatColor.RED + "The player [" + args[1] + "] is not online.");
                }
                break;
            case "item":
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mohist item info");
                    return false;
                }
                if ("info".equals(args[1].toLowerCase(Locale.ENGLISH))) {
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
            case "version":
                sender.sendMessage("Mohist: " + SharedConstants.VERSION_STRING);
                sender.sendMessage("Forge: " + ForgeVersion.getVersion());
                String[] cbs = CraftServer.class.getPackage().getImplementationVersion().split("-");
                sender.sendMessage("Bukkit: " + cbs[0]);
                sender.sendMessage("CraftBukkit: " + cbs[1]);
                sender.sendMessage("Spigot: " + cbs[2]);
                break;
            case "channels":
                sender.sendMessage(ServerAPI.channels.toString());
                break;
            case "speed":
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (args.length == 2 && p.isOp()) {
                        if (this.isFloat(args[1])) {
                            if (p.isFlying()) {
                                float speed = Float.parseFloat(args[1]);
                                if (speed >= 0.0f && speed < 21.0f) {
                                    p.setFlySpeed(speed / 10.0f);
                                    p.sendMessage("飞行速度已设置为 §b" + speed);
                                }
                            } else {
                                float speed = Float.parseFloat(args[1]);
                                if (speed >= 0.0f && speed < 21.0f) {
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
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
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
