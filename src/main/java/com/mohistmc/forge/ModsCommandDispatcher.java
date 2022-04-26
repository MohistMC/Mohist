package com.mohistmc.forge;

import com.mohistmc.MohistMC;
import com.mohistmc.api.ServerAPI;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.command.BukkitCommandWrapper;
import org.bukkit.help.GenericCommandHelpTopic;

public class ModsCommandDispatcher extends CommandDispatcher<CommandSource> {

    private final Commands commands;

    public ModsCommandDispatcher(Commands commands) {
        this.commands = commands;
    }

    @Override
    public LiteralCommandNode<CommandSource> register(LiteralArgumentBuilder<CommandSource> command) {
        LiteralCommandNode<CommandSource> node = command.build();
        if (Bukkit.getServer() != null && !(node.getCommand() instanceof BukkitCommandWrapper)) {
            ModCustomCommand wrapper = new ModCustomCommand(this.commands, node);
            CraftServer craftServer = (CraftServer) Bukkit.getServer();
            craftServer.getCommandMap().register("forge", wrapper);
            ServerAPI.forgecmdper.put(wrapper.getName(), wrapper.getPermission());
            craftServer.helpMap.addTopic(new GenericCommandHelpTopic(wrapper));
            MohistMC.LOGGER.debug("ModsCommandDispatcher register " + wrapper);
        }
        getRoot().addChild(node);
        return node;
    }
}
