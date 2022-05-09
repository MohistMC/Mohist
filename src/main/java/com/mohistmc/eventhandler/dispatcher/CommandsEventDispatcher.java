/*
 * MohistMC
 * Copyright (C) 2019-2022.
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

package com.mohistmc.eventhandler.dispatcher;

import com.mohistmc.api.ServerAPI;
import com.mohistmc.forge.ModCustomCommand;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.help.GenericCommandHelpTopic;

public class CommandsEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        if (Bukkit.getServer() instanceof CraftServer) {
            CraftServer craftServer = (CraftServer) Bukkit.getServer();
            Commands dispatcher = craftServer.getServer().getCommands();
            ModCustomCommand wrapper = new ModCustomCommand(dispatcher, event.getDispatcher().getRoot());
            craftServer.getCommandMap().register("forge", wrapper);
            ServerAPI.forgecmdper.put(wrapper.getName(), wrapper.getPermission());
            craftServer.helpMap.addTopic(new GenericCommandHelpTopic(wrapper));
            Bukkit.getLogger().info("ModsCommandDispatcher register " + wrapper.toString());
        }
    }
}
