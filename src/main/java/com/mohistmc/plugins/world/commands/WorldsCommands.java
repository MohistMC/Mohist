package com.mohistmc.plugins.world.commands;

import com.mohistmc.MohistMC;
import com.mohistmc.api.ItemAPI;
import com.mohistmc.plugins.MessageI18N;
import com.mohistmc.plugins.world.WorldManage;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import com.mohistmc.plugins.world.utils.WorldsGUI;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class WorldsCommands extends Command {
    public static String type;

    public WorldsCommands(String name) {
        super(name);
        this.description = "World Manager.";
        this.usageMessage = "/worlds";
        this.setPermission("mohist.command.worlds");
    }

    public static void worldNotExists(Player player, String world) {
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.command.thisWorld") + world + MohistMC.i18n.get("worldcommands.command.worldDontExist"));
    }

    public static void worldAllExists(Player player, String world) {
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.command.thisWorld") + world + MohistMC.i18n.get("worldcommands.command.worldExists"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (args.length == 0) {
            this.sendHelp(sender);
        }
        if (sender instanceof Player player) {
            if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {
                WorldsGUI.openWorldGui(player, MessageI18N.WORLDMANAGE_GUI_TITLE_1.getKey());
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("addtoconfig")) {
                ConfigByWorlds.addWorld(player.getWorld().getName());
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
                type = args[1].toLowerCase(java.util.Locale.ENGLISH);
                if (Bukkit.getWorld(args[1]) == null) {
                    int i = -1;
                    Inventory inv = Bukkit.createInventory(null, 27, MessageI18N.WORLDMANAGE_GUI_TITLE_0.getKey() + type);
                    for (World.Environment environment : World.Environment.values()) {
                        if (environment == World.Environment.CUSTOM) continue;
                        i++;
                        inv.setItem(i, ItemAPI.doItem(Material.MAP, 1, environment.name(), null));
                    }
                    player.openInventory(inv);
                } else {
                    worldAllExists(player, type);
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
                String worldName = args[1].toLowerCase(java.util.Locale.ENGLISH);
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    worldNotExists(player, worldName);
                    return false;
                }
                ConfigByWorlds.getSpawn(worldName, player);
                player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.command.teleport") + worldName + MohistMC.i18n.get("worldcommands.command.spawn"));
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
                ConfigByWorlds.getSpawn(player.getWorld().getName(), player);
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
                String worldName = args[1].toLowerCase(java.util.Locale.ENGLISH);
                if (!args[1].equalsIgnoreCase("world")) {
                    World w = Bukkit.getWorld(worldName);
                    if (w != null) {
                        for (Player all : w.getPlayers()) {
                            all.teleport(Bukkit.getWorld("world").getSpawnLocation());
                        }
                    }
                    try {
                        Bukkit.unloadWorld(w, true);
                        File deleteWorld = w.getWorldFolder();
                        WorldManage.deleteDir(deleteWorld);
                        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.delSuccessful"));
                        ConfigByWorlds.removeWorld(worldName);
                    } catch (Exception e2) {
                        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.delUnsuccessful"));
                    }
                } else {
                    player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.delDenied"));
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("import")) {
                String worldName = args[1].toLowerCase(java.util.Locale.ENGLISH);
                try {
                    World w = Bukkit.getWorld(worldName);
                    player.teleport(w.getSpawnLocation());
                    player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldExistsTele"));
                } catch (Exception e3) {
                    File loadWorld = new File(worldName); // TODO forge and bukkit world file path?
                    if (loadWorld.exists()) {
                        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "worldcommands.world.loadworld");
                        World w = Bukkit.createWorld(new WorldCreator(worldName));
                        Location location = Bukkit.getWorld(worldName).getSpawnLocation();
                        player.teleport(location);
                        ConfigByWorlds.addWorld(worldName);
                        ConfigByWorlds.addSpawn(location);
                        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.loadWorldSuccessful"));
                    } else {
                        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldFileNotfound"));
                    }
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("unload")) {
                String worldName = args[1].toLowerCase(java.util.Locale.ENGLISH);
                if (Bukkit.getWorld(worldName) == null) {
                    return false;
                }
                for (Player all2 : Bukkit.getWorld(worldName).getPlayers()) {
                    all2.teleport(Bukkit.getWorld("world").getSpawnLocation());
                }
                Bukkit.unloadWorld(Bukkit.getWorld(worldName), true);
                ConfigByWorlds.removeWorld(worldName);
                player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldUnload"));
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("addinfo")) {
                World w = player.getWorld();
                ConfigByWorlds.addInfo(w.getName(), args[1]);
                player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() +  MohistMC.i18n.get("worldcommands.world.worldSetupSuccess"));
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
                player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "Currently located in the world: " + player.getWorld().getName());
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("setname")) {
                String worldname = player.getWorld().getName();
                ConfigByWorlds.addname(worldname, args[1]);
                player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldSetupSuccess"));
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
                ConfigByWorlds.addSpawn(player.getLocation());
                player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldSetupSuccess"));
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("difficulty")) {
                if (WorldManage.isInteger(args[1])) {
                    int nandu = Integer.parseInt(args[1]);
                    if (nandu >= 0 && nandu < 4) {
                        if (nandu == 0) {
                            player.getWorld().setDifficulty(Difficulty.PEACEFUL);
                            ConfigByWorlds.setnandu(player, "PEACEFUL");
                            player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldSetupSuccess"));
                        } else if (nandu == 1) {
                            player.getWorld().setDifficulty(Difficulty.EASY);
                            ConfigByWorlds.setnandu(player, "EASY");
                            player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldSetupSuccess"));
                        } else if (nandu == 2) {
                            player.getWorld().setDifficulty(Difficulty.NORMAL);
                            ConfigByWorlds.setnandu(player, "NORMAL");
                            player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldSetupSuccess"));
                        } else if (nandu == 3) {
                            player.getWorld().setDifficulty(Difficulty.HARD);
                            ConfigByWorlds.setnandu(player, "HARD");
                            player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.worldSetupSuccess"));
                        }
                    } else {
                        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.setDifFailure"));
                    }
                } else {
                    player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + MohistMC.i18n.get("worldcommands.world.setDifFailure"));
                }
            }
        } else {
            if (args.length == 3 && args[0].equalsIgnoreCase("tp") && sender.isOp()) {
                for (Player target : Bukkit.getOnlinePlayers()) {
                    String name = target.getName();
                    String argsname = args[1];
                    if (Bukkit.getPlayer(argsname) == null) {
                        return false;
                    }
                    if (argsname.equals(name)) {
                        Player target1 = Bukkit.getServer().getPlayer(argsname);
                        String worldName = args[2].toLowerCase(Locale.ENGLISH);
                        World world = Bukkit.getWorld(worldName);
                        if (world == null) {
                            return false;
                        }
                        ConfigByWorlds.getSpawn(worldName, target1);
                        return true;
                    }
                }
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                List<String> c = new ArrayList<>();
                for (World world : Bukkit.getWorlds()) {
                    c.add(world.getName());
                }
                sender.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + c);
            }
        }
        return false;
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

    private final List<String> params = Arrays.asList("create", "delete", "tp", "import", "unload", "info", "addinfo", "setname", "setspawn", "gui", "difficulty");


    private void sendHelp(CommandSender player) {
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds create <Name> " + MohistMC.i18n.get("worldmanage.command.create"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds delete <Name> " + MohistMC.i18n.get("worldmanage.command.delete"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds tp <Name> " + MohistMC.i18n.get("worldmanage.command.tp"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds tp <Player> <Name> " + MohistMC.i18n.get("worldmanage.command.tp0"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds import <Name> " + MohistMC.i18n.get("worldmanage.command.unload"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds unload <Name> " + MohistMC.i18n.get("worldmanage.command.import"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds info " + MohistMC.i18n.get("worldmanage.command.info"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds addinfo <Name> " + MohistMC.i18n.get("worldmanage.command.addinfo"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds setname <Name> " + MohistMC.i18n.get("worldmanage.command.setname"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds setspawn " + MohistMC.i18n.get("worldmanage.command.setspawn"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds gui " + MohistMC.i18n.get("worldmanage.command.gui"));
        player.sendMessage(MessageI18N.WORLDMANAGE_PREFIX.getKey() + "/worlds difficulty <0-3>  " + MohistMC.i18n.get("worldmanage.command.difficulty"));
    }
}
