/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.forge;

import com.mohistmc.MohistMC;
import com.mohistmc.api.ServerAPI;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.command.BukkitCommandWrapper;
import org.bukkit.help.GenericCommandHelpTopic;

public class ModsCommandDispatcher extends CommandDispatcher<CommandSourceStack> {

    private final Commands commands;

    public ModsCommandDispatcher(Commands commands) {
        this.commands = commands;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> register(LiteralArgumentBuilder<CommandSourceStack> command) {
        LiteralCommandNode<CommandSourceStack> node = command.build();
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
