package com.mohistmc.eventhandler.dispatcher;

import com.mohistmc.paper.event.server.ServerTickStartEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEventDispatcher {

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        new ServerTickStartEvent(event.getServer().getTickCount() + 1).callEvent(); // Paper
    }
}
