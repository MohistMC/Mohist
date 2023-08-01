package com.mohistmc.commands;

import com.mohistmc.api.gui.GUIItem;
import com.mohistmc.api.gui.ItemStackFactory;
import com.mohistmc.api.gui.Warehouse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/1 20:00:00
 */
public class ShowCommand extends Command {

    public ShowCommand(String name) {
        super(name);
        this.description = "Mohist show commands";
        this.usageMessage = "/show [sounds]";
        this.setPermission("mohist.command.show");
    }

    private final List<String> params = Arrays.asList("sounds");

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

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }


        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to perform this command.");
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "sounds" -> {
                Warehouse wh = new Warehouse("Sounds");
                wh.getGUI().setItem(47, new GUIItem(new ItemStackFactory(Material.REDSTONE)
                        .setDisplayName("§cStop all sounds")
                        .toItemStack()) {
                    @Override
                    public void ClickAction(ClickType type, Player u) {
                        u.stopAllSounds();
                    }
                });
                for (Sound s : Sound.values()) {
                    wh.addItem(new GUIItem(new ItemStackFactory(Material.NOTE_BLOCK)
                            .setDisplayName(s.name())
                            .toItemStack()) {
                        @Override
                        public void ClickAction(ClickType type, Player u) {
                            player.playSound(player.getLocation(), s, 1f, 1.0f);
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
}
