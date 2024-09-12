package com.mohistmc.plugins.world.commands;

import com.mohistmc.api.ItemAPI;
import com.mohistmc.forge.ForgeInjectBukkit;
import com.mohistmc.plugins.world.WorldManage;
import com.mohistmc.plugins.world.listener.InventoryClickListener;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import com.mohistmc.plugins.world.utils.WorldInventory;
import com.mohistmc.plugins.world.utils.WorldInventoryType;
import com.mohistmc.plugins.world.utils.WorldsGUI;
import com.mohistmc.util.I18n;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class WorldsCommands extends Command {
    public static String type;

    public WorldsCommands(String name) {
        super(name);
        this.description = "World Manager.";
        this.usageMessage = "/worlds";
        this.setPermission("mohist.command.worlds");
    }

    public static void worldNotExists(Player player, String world) {
        player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.command.thisWorld") + world + I18n.as("worldcommands.command.worldDontExist"));
    }

    public static void worldAllExists(Player player, String world) {
        player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.command.thisWorld") + world + I18n.as("worldcommands.command.worldExists"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String currentAlias, String[] args) {
        if (args.length == 0) {
            this.sendHelp(sender);
            return false;
        }
        if (sender instanceof Player player) {
            if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {
                WorldsGUI.openWorldGui(player, I18n.as("worldmanage.gui.title1"));
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("addtoconfig")) {
                ConfigByWorlds.addWorld(player.getWorld().getName(), false);
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
                type = args[1].toLowerCase(java.util.Locale.ENGLISH);
                if (Bukkit.getWorld(args[1]) == null) {
                    int i = -1;
                    WorldInventory worldCreateInventory = new WorldInventory(
                            WorldInventoryType.CREATE,
                            27,
                            I18n.as("worldmanage.gui.title0") + type);
                    Inventory inventory = worldCreateInventory.getInventory();
                    for (World.Environment environment : ForgeInjectBukkit.environment.values()) {
                        if (environment == World.Environment.CUSTOM) continue;
                        i++;
                        inventory.setItem(i, ItemAPI.doItem(Material.MAP, 1, environment.name(), null));
                    }
                    inventory.addItem(ItemAPI.doItem(Material.MAP, 1, "void", null));
                    player.openInventory(inventory);
                    InventoryClickListener.worldInventory = worldCreateInventory;
                } else {
                    worldAllExists(player, type);
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
                String worldName = args[1];
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    worldNotExists(player, worldName);
                    return false;
                }
                ConfigByWorlds.getSpawn(worldName, player);
                player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.command.teleport") + worldName + I18n.as("worldcommands.command.spawn"));
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
                ConfigByWorlds.getSpawn(player.getWorld().getName(), player);
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
                String worldName = args[1];
                if (!args[1].equalsIgnoreCase("world")) {
                    World w = Bukkit.getWorld(worldName);
                    if (w != null) {
                        for (Player all : w.getPlayers()) {
                            all.teleport(MinecraftServer.getServer().overworld().world.getSpawnLocation()); // use overworld
                        }
                        try {
                            ConfigByWorlds.removeWorld(worldName);
                            Bukkit.unloadWorld(w, true);
                            File deleteWorld = w.getWorldFolder();
                            WorldManage.deleteDir(deleteWorld);
                            player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.delSuccessful"));
                        } catch (Exception e2) {
                            player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.delUnsuccessful"));
                        }
                    }
                } else {
                    player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.delDenied"));
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("import")) {
                String worldName = args[1].toLowerCase(java.util.Locale.ENGLISH);
                try {
                    World w = Bukkit.getWorld(worldName);
                    player.teleport(w.getSpawnLocation());
                    player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldExistsTele"));
                } catch (Exception e3) {
                    File loadWorld = new File(worldName); // TODO forge and bukkit world file path?
                    if (loadWorld.exists()) {
                        player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.loadworld"));
                        Bukkit.createWorld(new WorldCreator(worldName));
                        World w = Bukkit.getWorld(worldName);
                        Location location = w.getSpawnLocation();
                        player.teleport(location);
                        ConfigByWorlds.addWorld(worldName, true);
                        ConfigByWorlds.addSpawn(location);
                        player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.loadWorldSuccessful"));
                    } else {
                        player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldFileNotfound"));
                    }
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("unload")) {
                String worldName = args[1];
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    return false;
                }
                for (Player all2 : world.getPlayers()) {
                    all2.teleport(Bukkit.getWorld("world").getSpawnLocation());
                }
                Bukkit.unloadWorld(world, true);
                ConfigByWorlds.removeWorld(worldName);
                player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldUnload"));
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("addinfo")) {
                World w = player.getWorld();
                ConfigByWorlds.addInfo(w.getName(), args[1]);
                player.sendMessage(I18n.as("worldmanage.prefix") +  I18n.as("worldcommands.world.worldSetupSuccess"));
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
                player.sendMessage(I18n.as("worldmanage.prefix") + "Currently located in the world: " + player.getWorld().getName());
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("setname")) {
                String worldname = player.getWorld().getName();
                ConfigByWorlds.addname(worldname, args[1]);
                player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldSetupSuccess"));
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
                ConfigByWorlds.addSpawn(player.getLocation());
                player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldSetupSuccess"));
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("difficulty")) {
                if (WorldManage.isInteger(args[1])) {
                    int nandu = Integer.parseInt(args[1]);
                    if (nandu >= 0 && nandu < 4) {
                        if (nandu == 0) {
                            player.getWorld().setDifficulty(Difficulty.PEACEFUL);
                            ConfigByWorlds.setnandu(player, "PEACEFUL");
                            player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldSetupSuccess"));
                        } else if (nandu == 1) {
                            player.getWorld().setDifficulty(Difficulty.EASY);
                            ConfigByWorlds.setnandu(player, "EASY");
                            player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldSetupSuccess"));
                        } else if (nandu == 2) {
                            player.getWorld().setDifficulty(Difficulty.NORMAL);
                            ConfigByWorlds.setnandu(player, "NORMAL");
                            player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldSetupSuccess"));
                        } else if (nandu == 3) {
                            player.getWorld().setDifficulty(Difficulty.HARD);
                            ConfigByWorlds.setnandu(player, "HARD");
                            player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.worldSetupSuccess"));
                        }
                    } else {
                        player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.setDifFailure"));
                    }
                } else {
                    player.sendMessage(I18n.as("worldmanage.prefix") + I18n.as("worldcommands.world.setDifFailure"));
                }
            }
        } else {
            if (args.length == 3 && args[0].equalsIgnoreCase("tp") && sender.isOp()) {
                for (Player target : Bukkit.getOnlinePlayers()) {
                    String name = target.getName();
                    String argsname = args[1];
                    Player target1 = Bukkit.getPlayer(name);
                    if (target1 == null) {
                        return false;
                    }
                    if (argsname.equals(name)) {
                        String worldName = args[2];
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
                sender.sendMessage(I18n.as("worldmanage.prefix") + c);
            }
        }
        return false;
    }

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

        if (args.length == 2 && args[0].equalsIgnoreCase("item")) {
            list.add("info");
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("tp")) {
            list.addAll(((CraftServer)Bukkit.getServer()).getWorldsByName().stream().toList());
        }

        return list;
    }

    private final List<String> params = Arrays.asList("create", "delete", "tp", "import", "unload", "info", "addinfo", "setname", "setspawn", "gui", "difficulty");


    private void sendHelp(CommandSender player) {
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds create <Name> " + I18n.as("worldmanage.command.create"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds delete <Name> " + I18n.as("worldmanage.command.delete"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds tp <Name> " + I18n.as("worldmanage.command.tp"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds tp <Player> <Name> " + I18n.as("worldmanage.command.tp0"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds import <Name> " + I18n.as("worldmanage.command.import"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds unload <Name> " + I18n.as("worldmanage.command.unload"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds info " + I18n.as("worldmanage.command.info"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds addinfo <Name> " + I18n.as("worldmanage.command.addinfo"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds setname <Name> " + I18n.as("worldmanage.command.setname"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds setspawn " + I18n.as("worldmanage.command.setspawn"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds gui " + I18n.as("worldmanage.command.gui"));
        player.sendMessage(I18n.as("worldmanage.prefix") + "/worlds difficulty <0-3>  " + I18n.as("worldmanage.command.difficulty"));
    }
}
