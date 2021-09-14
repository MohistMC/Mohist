package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldEventDispatcher {

    //For WorldLoadEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldLoadEvent(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld handle = ((CraftWorld) event.getWorld()).getHandle();
            WorldLoadEvent load = new WorldLoadEvent(handle.getWorld());
            Bukkit.getPluginManager().callEvent(load);
        }
    }

    //For WorldSaveEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldSaveEvent(WorldEvent.Save event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld handle = ((CraftWorld) event.getWorld()).getHandle();
            WorldSaveEvent save = new WorldSaveEvent(handle.getWorld());
            Bukkit.getPluginManager().callEvent(save);
        }
    }
}
