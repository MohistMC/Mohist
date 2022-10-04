package com.mohistmc.forge;

import com.mohistmc.api.event.BukkitHookForgeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBusInvokeDispatcher;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.bukkit.Bukkit;

public class ForgeEventHandler implements IEventBusInvokeDispatcher {

    public static void init() {
        try {
            ObfuscationReflectionHelper.setPrivateValue(Class.forName("net.minecraftforge.eventbus.EventBus"), null, new ForgeEventHandler(), "hookDisPatcher");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void invoke(IEventListener eventListener, Event event) {
        if (BukkitHookForgeEvent.getHandlerList().getRegisteredListeners().length > 0) {
            Bukkit.getPluginManager().callEvent(new BukkitHookForgeEvent(event));
        }
    }
}
