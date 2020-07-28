package org.bukkit.craftbukkit.v1_16_R1.command;

import com.google.common.base.Joiner;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.tree.CommandNode;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;

public final class VanillaCommandWrapper extends BukkitCommand {

    private final CommandManager dispatcher;
    public final CommandNode<?> vanillaCommand;

    public VanillaCommandWrapper(CommandManager dispatcher, CommandNode<?> vanillaCommand) {
        super(vanillaCommand.getName(), "A Minecraft provided command", vanillaCommand.getUsageText(), Collections.emptyList());
        this.dispatcher = dispatcher;
        this.vanillaCommand = vanillaCommand;
        this.setPermission(getPermission(vanillaCommand));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        dispatcher.execute(getCommandSource(sender), toDispatcher(args, commandLabel));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        ServerCommandSource icommandlistener = getCommandSource(sender);
        ParseResults<ServerCommandSource> parsed = dispatcher.getDispatcher().parse(toDispatcher(args, getName()), icommandlistener);

        List<String> results = new ArrayList<>();
        dispatcher.getDispatcher().getCompletionSuggestions(parsed).thenAccept(suggestions -> suggestions.getList().forEach(s -> results.add(s.getText())));

        return results;
    }

    public static String getPermission(CommandNode<?> vanillaCommand) {
        return "minecraft.command." + ((vanillaCommand.getRedirect() == null) ? vanillaCommand.getName() : vanillaCommand.getRedirect().getName());
    }

    private String toDispatcher(String[] args, String name) {
        return name + ((args.length > 0) ? " " + Joiner.on(' ').join(args) : "");
    }

    private ServerCommandSource getCommandSource(CommandSender s) {
        if (s instanceof CraftPlayer)
            return ((CraftPlayer)s).getHandle().getCommandSource();
        if (s instanceof CraftEntity)
            return ((CraftEntity)s).getHandle().getCommandSource();
        if (s instanceof ConsoleCommandSender)
            return ((CraftServer) s.getServer()).getServer().getCommandSource();

        return null;
    }

}