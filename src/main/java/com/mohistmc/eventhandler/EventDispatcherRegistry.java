package com.mohistmc.eventhandler;

import com.mohistmc.MohistMC;
import com.mohistmc.eventhandler.dispatcher.BlockEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.EntityEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.PlayerEventDispatcher;
import net.minecraftforge.common.MinecraftForge;

public class EventDispatcherRegistry {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EntityEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new BlockEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new PlayerEventDispatcher());
        MohistMC.LOGGER.info("EventDispatcherRegistry initialized");
    }
}
