package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldEventDispatcher {

    //For WorldLoadEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldLoadEvent(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld handle = (ServerWorld) event.getWorld();
            WorldLoadEvent load = new WorldLoadEvent(handle.getWorld());
            Bukkit.getPluginManager().callEvent(load);
        }
    }

    //For WorldSaveEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldSaveEvent(WorldEvent.Save event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld handle = (ServerWorld) event.getWorld();
            WorldSaveEvent save = new WorldSaveEvent(handle.getWorld());
            Bukkit.getPluginManager().callEvent(save);
        }
    }
}
