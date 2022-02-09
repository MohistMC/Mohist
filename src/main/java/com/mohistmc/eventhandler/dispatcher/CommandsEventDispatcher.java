package com.mohistmc.eventhandler.dispatcher;

import com.mohistmc.api.ServerAPI;
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
            VanillaCommandWrapper wrapper = new VanillaCommandWrapper(dispatcher, event.getDispatcher().getRoot());
            craftServer.getCommandMap().register("forge", wrapper);
            ServerAPI.forgecmdper.put(wrapper.getName(), wrapper.getPermission());
            Bukkit.getLogger().info("ModsCommandDispatcher register " + wrapper.toString());
        }
    }
}
