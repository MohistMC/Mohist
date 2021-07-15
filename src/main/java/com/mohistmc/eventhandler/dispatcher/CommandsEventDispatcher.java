package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper;

public class CommandsEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        if (Bukkit.getServer() instanceof CraftServer) {
            CraftServer craftServer = (CraftServer) Bukkit.getServer();
            Commands dispatcher = craftServer.getServer().getCommands();
            craftServer.getCommandMap().register("minecraft", new VanillaCommandWrapper(dispatcher, event.getDispatcher().getRoot()));
        }
    }
}
