package org.bukkit.command;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftFunctionCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftRemoteConsoleCommandSender;

public class CommandWrapper implements ICommand {
    private final CommandSender sender;
    private final String name;
    private final Command command;

    public CommandWrapper(CommandSender sender, String name, Command command) {
        this.sender = sender;
        this.command = command;
        this.name = name;
    }

    @Nullable
    public static CommandWrapper toNMSCommand(ICommandSender sender, String name) {
        Command command;
        CommandSender bukkitSender;
        if ((command = MinecraftServer.getServerInst().server.getCommandMap().getCommand(name)) != null && (bukkitSender = toBukkitSender(sender)) != null) {
            return new CommandWrapper(bukkitSender, name, command);
        }
        return null;
    }

    @Nullable
    public static CommandSender toBukkitSender(ICommandSender sender) {
        if (sender instanceof MinecraftServer) {
            return MinecraftServer.getServerInst().console;
        }
        if (sender instanceof RConConsoleSource) {
            return new CraftRemoteConsoleCommandSender((RConConsoleSource) sender);
        }
        if (sender instanceof CommandBlockBaseLogic) {
            return new CraftBlockCommandSender(sender);
        }
        if (sender instanceof FunctionManager.CustomFunctionListener) {
            return new CraftFunctionCommandSender(sender);
        }
        if (sender instanceof Entity) {
            return ((Entity) sender).getBukkitEntity();
        }
        return null;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getName() {
        return this.command.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return this.command.getDescription();
    }

    @Override
    public List<String> getAliases() {
        return this.command.getAliases();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender icsender, String[] args) throws CommandException {
        try {
            this.command.execute(sender, name, args);
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender icsender) {
        return this.command.testPermission(sender);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender icsender, String[] args, BlockPos targetPos) {
        try {
            return this.command.tabComplete(sender, name, args);
        } catch (Exception e) {
            e.printStackTrace();
            return ImmutableList.of();
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
