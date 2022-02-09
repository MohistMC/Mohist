package com.mohistmc.eventhandler;

import com.mohistmc.MohistMC;
import com.mohistmc.eventhandler.dispatcher.BlockEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.BucketEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.CommandsEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.EntityEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.ItemEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.PlayerEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.WorldEventDispatcher;
import net.minecraftforge.common.MinecraftForge;

public class EventDispatcherRegistry {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new BlockEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new PlayerEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new BucketEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new CommandsEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new WorldEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new ItemEventDispatcher());
        MinecraftForge.EVENT_BUS.register(new EntityEventDispatcher());
        MohistMC.LOGGER.info("EventDispatcherRegistry initialized");
    }
}
