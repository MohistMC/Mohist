package com.destroystokyo.paper;

import com.destroystokyo.paper.config.PaperConfig;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.addons.server.MCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PaperCommand extends Command {
    private static final String BASE_PERM = "bukkit.command.paper.";
    private static final ImmutableSet<String> SUBCOMMANDS = ImmutableSet.<String>builder().add("heap", "entity", "reload", "version", "debug", "chunkinfo").build();

    public PaperCommand(String name) {
        super(name);
        this.description = "Paper related commands";
        this.usageMessage = "/paper [" + Joiner.on(" | ").join(SUBCOMMANDS) + "]";
        this.setPermission("bukkit.command.paper;" + Joiner.on(';').join(SUBCOMMANDS.stream().map(s -> BASE_PERM + s).collect(Collectors.toSet())));
    }

    private static boolean testPermission(CommandSender commandSender, String permission) {
        if (commandSender.hasPermission(BASE_PERM + permission) || commandSender.hasPermission("bukkit.command.paper"))
            return true;
        commandSender.sendMessage(Bukkit.getPermissionMessage());
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length <= 1)
            return getListMatchingLast(sender, args, SUBCOMMANDS);

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "entity":
                if (args.length == 2)
                    return getListMatchingLast(sender, args, "help", "list");
                if (args.length == 3)
                    return getListMatchingLast(sender, args, EntityType.getEntityNameList().stream().map(ResourceLocation::toString).sorted().toArray(String[]::new));
                break;
            case "debug":
                if (args.length == 2) {
                    return getListMatchingLast(sender, args, "help", "chunks");
                }
                break;
            case "chunkinfo":
                List<String> worldNames = new ArrayList<>();
                worldNames.add("*");
                for (org.bukkit.World world : Bukkit.getWorlds()) {
                    worldNames.add(world.getName());
                }
                if (args.length == 2) {
                    return getListMatchingLast(sender, args, worldNames);
                }
                break;
        }
        return Collections.emptyList();
    }

    // Code from Mojang - copyright them
    public static List<String> getListMatchingLast(CommandSender sender, String[] args, String... matches) {
        return getListMatchingLast(sender, args, (Collection) Arrays.asList(matches));
    }

    public static boolean matches(String s, String s1) {
        return s1.regionMatches(true, 0, s, 0, s.length());
    }

    public static List<String> getListMatchingLast(CommandSender sender, String[] strings, Collection<?> collection) {
        String last = strings[strings.length - 1];
        ArrayList<String> results = Lists.newArrayList();

        if (!collection.isEmpty()) {
            Iterator iterator = Iterables.transform(collection, Functions.toStringFunction()).iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                if (matches(last, s1) && (sender.hasPermission(BASE_PERM + s1) || sender.hasPermission("bukkit.command.paper"))) {
                    results.add(s1);
                }
            }

            if (results.isEmpty()) {
                iterator = collection.iterator();

                while (iterator.hasNext()) {
                    Object object = iterator.next();

                    if (object instanceof ResourceLocation && matches(last, ((ResourceLocation) object).getPath())) {
                        results.add(String.valueOf(object));
                    }
                }
            }
        }

        return results;
    }
    // end copy stuff

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }
        if (SUBCOMMANDS.contains(args[0].toLowerCase(Locale.ENGLISH))) {
            if (!testPermission(sender, args[0].toLowerCase(Locale.ENGLISH))) return true;
        }
        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "heap":
                dumpHeap(sender);
                break;
            case "reload":
                doReload(sender);
                break;
            case "debug":
                doDebug(sender, args);
                break;
            case "ver":
                if (!testPermission(sender, "version"))
                    break; // "ver" needs a special check because it's an alias. All other commands are checked up before the switch statement (because they are present in the SUBCOMMANDS set)
            case "version":
                Command ver = org.bukkit.Bukkit.getServer().getCommandMap().getCommand("version");
                if (ver != null) {
                    ver.execute(sender, commandLabel, new String[0]);
                    break;
                }
                // else - fall through to default
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }

    private void doDebug(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Use /paper debug [chunks] help for more information on a specific command");
            return;
        }

        String debugType = args[1].toLowerCase(Locale.ENGLISH);
        switch (debugType) {
            case "chunks":
                if (args.length >= 3 && args[2].toLowerCase(Locale.ENGLISH).equals("help")) {
                    sender.sendMessage(ChatColor.RED + "Use /paper debug chunks to dump loaded chunk information to a file");
                    break;
                }
                File file = new File(new File(new File("."), "debug"),
                        "chunks-" + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss").format(LocalDateTime.now()) + ".txt");
                sender.sendMessage(ChatColor.GREEN + "Writing chunk information dump to " + file.toString());
                try {
                    sender.sendMessage(ChatColor.GREEN + "Successfully written chunk information!");
                } catch (Throwable thr) {
                    MinecraftServer.LOGGER.warn("Failed to dump chunk information to file " + file.toString(), thr);
                    sender.sendMessage(ChatColor.RED + "Failed to dump chunk information, see console");
                }

                break;
            case "help":
                // fall through to default
            default:
                sender.sendMessage(ChatColor.RED + "Use /paper debug [chunks] help for more information on a specific command");
                return;
        }
    }

    private void dumpHeap(CommandSender sender) {
        // Mohist - fix https://github.com/MohistMC/Mohist/issues/1420 ?
        /*java.nio.file.Path dir = java.nio.file.Paths.get("./dumps");
        String name = "heap-dump-" + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss").format(LocalDateTime.now());

        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Writing JVM heap data...");

        java.nio.file.Path file = CraftServer.dumpHeap(dir, name);
        if (file != null) {
            Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Heap dump saved to " + file);
        } else {
            Command.broadcastCommandMessage(sender, ChatColor.RED + "Failed to write heap dump, see sever log for details");
        }*/
    }

    private void doReload(CommandSender sender) {
        Command.broadcastCommandMessage(sender, ChatColor.RED + "Please note that this command is not supported and may cause issues.");
        Command.broadcastCommandMessage(sender, ChatColor.RED + "If you encounter any issues please use the /stop command to restart your server.");

        MinecraftServer console = MinecraftServer.getServer();
        PaperConfig.init((File) console.options.valueOf("paper-settings"));
        for (ServerWorld world : console.getAllLevels()) {
            //world.paperConfig.init();
        }
        console.server.reloadCount++;

        Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Paper config reload complete.");
    }
}
