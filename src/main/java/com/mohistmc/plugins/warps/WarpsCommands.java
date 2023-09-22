package com.mohistmc.plugins.warps;

import com.mohistmc.MohistMC;
import com.mohistmc.api.gui.GUIItem;
import com.mohistmc.api.gui.ItemStackFactory;
import com.mohistmc.api.gui.Warehouse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
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
                        WarpsUtils.add(player.getLocation(), name);
                        player.sendMessage(MohistMC.i18n.get("warpscommands.set.success", name));
                        return true;
                    }
                    case "del" -> {
                        String name = args[1];
                        if (WarpsUtils.has(name)) {
                            WarpsUtils.del(name);
                            player.sendMessage(MohistMC.i18n.get("warpscommands.nowarp"));
                            return true;
                        } else {
                            player.sendMessage(MohistMC.i18n.get("warpscommands.del.success", name));
                            return false;
                        }
                    }
                    case "tp" -> {
                        String name = args[1];
                        if (WarpsUtils.has(name)) {
                            player.teleport(WarpsUtils.get(name));
                            return true;
                        } else {
                            player.sendMessage(MohistMC.i18n.get("warpscommands.nowarp"));
                            return false;
                        }
                    }
                }
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {
                Warehouse wh = new Warehouse(MohistMC.i18n.get("warpscommands.prefix"));
                for (String w : WarpsUtils.config.getKeys(false)) {
                    wh.addItem(new GUIItem(new ItemStackFactory(Material.BAMBOO_SIGN)
                            .setDisplayName(w)
                            .setLore(List.of(MohistMC.i18n.get("warpscommands.gui.click"), "§f" + WarpsUtils.get(w).asString()))
                            .toItemStack()) {
                        @Override
                        public void ClickAction(ClickType type, Player u, ItemStack itemStack) {
                            u.teleport(WarpsUtils.get(w));
                        }
                    });
                }
                wh.openGUI(player);
                return true;
            }
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("tp")) {
            String playerName = args[1];
            String warpsName = args[2];
            if (Bukkit.getPlayer(playerName) == null) {
                sender.sendMessage(MohistMC.i18n.get("warpscommands.noplayer"));
                return false;
            }
            if (WarpsUtils.has(warpsName)) {
                Bukkit.getPlayer(playerName).teleport(WarpsUtils.get(warpsName));
                return true;
            } else {
                sender.sendMessage(MohistMC.i18n.get("warpscommands.nowarp"));
                return false;
            }

        }
        return false;
    }

    private final List<String> params = Arrays.asList("set", "del", "tp", "gui");


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
        return list;
    }


    private void sendHelp(CommandSender player) {
        String prefix = MohistMC.i18n.get("warpscommands.prefix");
        player.sendMessage(prefix + " /warps set <Name> " + MohistMC.i18n.get("warpscommands.set"));
        player.sendMessage(prefix + " /warps del <Name> " + MohistMC.i18n.get("warpscommands.del"));
        player.sendMessage(prefix + " /warps tp <Name> " + MohistMC.i18n.get("warpscommands.tp"));
        player.sendMessage(prefix + " /warps tp <Player> <Name> " + MohistMC.i18n.get("warpscommands.tp0"));
        player.sendMessage(prefix + " /warps gui " + MohistMC.i18n.get("warpscommands.gui"));
    }
}
