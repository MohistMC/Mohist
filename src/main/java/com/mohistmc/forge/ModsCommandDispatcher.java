package com.mohistmc.forge;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper;

public class ModsCommandDispatcher extends CommandDispatcher<CommandSource> {

    private final Commands commands;

    public ModsCommandDispatcher(Commands commands) {
        this.commands = commands;
    }

    @Override
    public LiteralCommandNode<CommandSource> register(LiteralArgumentBuilder<CommandSource> command) {
        LiteralCommandNode<CommandSource> node = command.build();
        if (Bukkit.getServer() != null && !(node.getCommand() instanceof BukkitCommandWrapper)) {
            VanillaCommandWrapper wrapper = new VanillaCommandWrapper(this.commands, node);
            ((CraftServer) Bukkit.getServer()).getCommandMap().register("forge", wrapper);
            Bukkit.getLogger().info("ModsCommandDispatcher register " + wrapper.toString());
        }
        getRoot().addChild(node);
        return node;
    }
}
