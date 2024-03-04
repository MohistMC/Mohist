package com.mohistmc.plugins.warps;

import com.mohistmc.api.WarpAPI;
import com.mohistmc.api.gui.GUIItem;
import com.mohistmc.api.gui.Warehouse;
import com.mohistmc.api.item.MohistItem;
import com.mohistmc.api.location.LocationAPI;
import com.mohistmc.api.location.LocationAPI.ClosestDistance;
import com.mohistmc.util.I18n;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/12 16:27:32
 */
public class WarpsCommands extends Command {

    public WarpsCommands(String name) {
        super(name);
        this.description = "Warps Manager.";
        this.usageMessage = "/warps";
        this.setPermission("mohist.command.warps");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length == 0) {
            this.sendHelp(sender);
            return false;
        }
        if (sender instanceof Player player) {
            if (args.length == 2) {
                switch (args[0].toLowerCase(Locale.ENGLISH)) {
                    case "set" -> {
                        String name = args[1];
                        WarpAPI.add(player.getLocation(), name);
                        player.sendMessage(I18n.as("warpscommands.set.success", name));
                        return true;
                    }
                    case "del" -> {
                        String name = args[1];
                        if (WarpAPI.has(name)) {
                            WarpAPI.del(name);
                            player.sendMessage(I18n.as("warpscommands.nowarp"));
                            return true;
                        } else {
                            player.sendMessage(I18n.as("warpscommands.del.success", name));
                            return false;
                        }
                    }
                    case "tp" -> {
                        String name = args[1];
                        if (WarpAPI.has(name)) {
                            WarpAPI.teleport(player, name);
                            return true;
                        } else {
                            player.sendMessage(I18n.as("warpscommands.nowarp"));
                            return false;
                        }
                    }
                }
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {
                Warehouse wh = new Warehouse(I18n.as("warpscommands.prefix"));
                Location playerLoc = player.getLocation();
                for (String w : WarpAPI.config.getKeys(false)) {
                    Location warpLoc = WarpAPI.get(w);
                    MohistItem guiItem = MohistItem.create(Material.BAMBOO_HANGING_SIGN)
                            .setDisplayName(w)
                            .setDisplayLore(WarpAPI.asStringList(w));

                    if (Objects.equals(warpLoc.getWorld(), playerLoc.getWorld())) {
                        guiItem.addDisplayLore("&6距离: &2%s格".formatted(LocationAPI.distanceBetweenLocation(playerLoc, warpLoc)));
                    }
                    wh.addItem(new GUIItem(guiItem.build()) {
                        @Override
                        public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                            u.teleport(warpLoc);
                        }
                    });
                }

                ClosestDistance result = LocationAPI.findClosest(WarpAPI.asList(), playerLoc);
                String name = WarpAPI.getName(result.location());
                if (name != null) {
                    if (Objects.equals(result.location().getWorld(), playerLoc.getWorld())) {
                        MohistItem guiItem = MohistItem.create(Material.WARPED_HANGING_SIGN)
                                .setDisplayName(name)
                                .setDisplayLore(WarpAPI.asStringList(name));

                        guiItem.addDisplayLore("&7========");
                        guiItem.addDisplayLore("&6最近的传送点");
                        guiItem.addDisplayLore("&6最近距离: &2%s格".formatted(result.distance()));
                        guiItem.addDisplayLore("&7========");
                        wh.getGUI().setItem(49, new GUIItem(guiItem.build()) {
                            @Override
                            public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                                u.teleport(result.location());
                            }
                        });
                    }
                }

                wh.openGUI(player);
                return true;
            }
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("tp")) {
            String playerName = args[1];
            String warpsName = args[2];
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                sender.sendMessage(I18n.as("warpscommands.noplayer"));
                return false;
            }
            if (WarpAPI.has(warpsName)) {
                WarpAPI.teleport(player, warpsName);
                return true;
            } else {
                sender.sendMessage(I18n.as("warpscommands.nowarp"));
                return false;
            }

        }
        return false;
    }

    private final List<String> params = Arrays.asList("set", "del", "tp", "gui");

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
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

    private void sendHelp(CommandSender player) {
        String prefix = I18n.as("warpscommands.prefix");
        player.sendMessage(prefix + " /warps set <Name> " + I18n.as("warpscommands.set"));
        player.sendMessage(prefix + " /warps del <Name> " + I18n.as("warpscommands.del"));
        player.sendMessage(prefix + " /warps tp <Name> " + I18n.as("warpscommands.tp"));
        player.sendMessage(prefix + " /warps tp <Player> <Name> " + I18n.as("warpscommands.tp0"));
        player.sendMessage(prefix + " /warps gui " + I18n.as("warpscommands.gui"));
    }
}
