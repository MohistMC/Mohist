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

import com.mohistmc.api.ItemAPI;
import com.mohistmc.api.PlayerAPI;
import com.mohistmc.api.gui.GUIItem;
import com.mohistmc.api.gui.Warehouse;
import com.mohistmc.plugins.item.ItemsConfig;
import com.mohistmc.util.I18n;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemsCommand extends Command {

    private final List<String> params = Arrays.asList("info", "name", "save", "remove", "list", "get");

    public ItemsCommand(String name) {
        super(name);
        this.description = I18n.as("itemscmd.description");
        this.usageMessage = "/items [info|name|save|list|get|remove]";
        this.setPermission("mohist.command.items");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
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

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "info" -> {
                if (itemStack == null || itemStack.getType().isAir()) {
                    player.sendMessage(ChatColor.RED + I18n.as("itemscmd.mainhandEmpty"));
                    return false;
                }
                ItemsCommand.info(player);
                return true;
            }
            case "name" -> {
                if (itemStack == null || itemStack.getType().isAir()) {
                    player.sendMessage(ChatColor.RED + I18n.as("itemscmd.mainhandEmpty"));
                    return false;
                }
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /items name <string>");
                    return false;
                }
                ItemAPI.name(player.getInventory().getItemInMainHand(), args[1]);
                sender.sendMessage(ChatColor.GREEN + "Item name set complete.");
                return true;
            }
            case "save" -> {
                if (itemStack == null || itemStack.getType().isAir()) {
                    player.sendMessage(ChatColor.RED + I18n.as("itemscmd.mainhandEmpty"));
                    return false;
                }
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /items save <name>");
                    return false;
                }
                ItemsConfig.INSTANCE.put("items." + args[1], itemStack);
                sender.sendMessage(ChatColor.GREEN + I18n.as("itemscmd.completeSet"));
                return true;
            }
            case "lore" -> {
                if (itemStack == null || itemStack.getType().isAir()) {
                    player.sendMessage(ChatColor.RED + I18n.as("itemscmd.mainhandEmpty"));
                    return false;
                }
                ItemAPI.lore(itemStack, List.of("§4test §2LO§3RE"));
                sender.sendMessage(ChatColor.GREEN + I18n.as("itemscmd.completeLore"));
                return true;
            }
            case "get" -> {
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /items get <name>");
                    return false;
                }
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(ItemsConfig.INSTANCE.get(args[1]));
                } else {
                    sender.sendMessage(ChatColor.GREEN + I18n.as("itemscmd.inventoryFull"));
                    return false;
                }
                return true;
            }
            case "remove" -> {
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /items remove <name>");
                    return false;
                }
                ItemsConfig.INSTANCE.remove(args[1]);
                sender.sendMessage(ChatColor.GREEN + I18n.as("itemscmd.removedItemp1") + args[1] + ChatColor.GREEN + I18n.as("itemscmd.removedItemp2") );
                return true;
            }
            case "list" ->{
                Warehouse wh = new Warehouse("Items");
                for (ItemStack s : ItemsConfig.INSTANCE.getItems()) {
                    wh.addItem(new GUIItem(s) {
                        @Override
                        public void ClickAction(ClickType type, Player u, ItemStack itemStack1) {
                            if (player.getInventory().firstEmpty() != -1) {
                                player.getInventory().addItem(itemStack1);
                            }
                        }
                    });
                }
                wh.openGUI(player);
                return true;
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
            }
        }
    }

    public static void info(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        sendMessageByCopy(player, ChatColor.GRAY + "Type - ", itemStack.getType().name());
        player.sendMessage(ChatColor.GRAY + "Name - %s".formatted(nmsItem.getHoverName().getString()));
        player.sendMessage(ChatColor.GRAY + "ForgeItem - %s".formatted(itemStack.getType().isForgeItem));
        player.sendMessage(ChatColor.GRAY + "ForgeBlock - %s".formatted(itemStack.getType().isForgeBlock));
        sendMessageByCopy(player, ChatColor.GRAY + "NBT(CraftBukkit) - ", ItemAPI.getNBTAsString(itemStack));
        sendMessageByCopy(player, ChatColor.GRAY + "NBT(Vanilla) - ", ItemAPI.getNbtAsString(PlayerAPI.getNMSPlayer(player).getMainHandItem().getTag()));
        sendMessageByCopy(player, ChatColor.GRAY + "NBT(Forge) - ", ItemAPI.getNbtAsString(nmsItem.getForgeCaps()));
    }

    public static void sendMessageByCopy(Player player, String des, String info) {
        TextComponent textComponent = new TextComponent(des + info);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder("§c%s".formatted(I18n.as("itemscmd.copy"))).create())));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, info));
        player.spigot().sendMessage(textComponent);
    }
}
