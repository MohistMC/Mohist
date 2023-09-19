package com.mohistmc.forge;

import com.mohistmc.api.event.BukkitHookForgeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBusInvokeDispatcher;
import net.minecraftforge.eventbus.api.IEventListener;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.bukkit.Bukkit;

@Deprecated(forRemoval = true, since = "1.20.2")
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
        Bukkit.getPluginManager().callEvent(new BukkitHookForgeEvent(event));
    }
}
