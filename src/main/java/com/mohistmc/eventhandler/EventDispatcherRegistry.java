package com.mohistmc.eventhandler;

import com.mohistmc.MohistMC;
import net.minecraftforge.common.MinecraftForge;

public class EventDispatcherRegistry {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new BlockEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new PlayerEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityEventDispatcher());
        MohistMC.LOGGER.info("EventDispatcherRegistry initialized");
    }
}
