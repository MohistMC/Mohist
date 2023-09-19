package com.mohistmc.eventhandler;

import com.mohistmc.MohistMC;
import com.mohistmc.eventhandler.dispatcher.BlockEventDispatcher;
import com.mohistmc.eventhandler.dispatcher.NoteBlockEventHook;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author Mgazul by MohistMC
 * @date 2023/3/19 14:49:49
 */
public class EventDispatcherRegistry {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new NoteBlockEventHook());
        MinecraftForge.EVENT_BUS.register(new BlockEventDispatcher());
        MohistMC.LOGGER.info("EventDispatcherRegistry initialized");
    }
}
