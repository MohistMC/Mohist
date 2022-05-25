package com.mohistmc.forge;

import com.mohistmc.api.event.BukkitHookForgeEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import org.bukkit.Bukkit;

public class CustomEventBus extends EventBus {

    public static final CustomEventBus BUS = new CustomEventBus(BusBuilder.builder());
    public CustomEventBus(BusBuilder busBuilder) {
        super(busBuilder);
    }

    @Override
    public boolean post(Event event)
    {
        if (Bukkit.getServer() != null) {
            BukkitHookForgeEvent bukkitHookForgeEvent = new BukkitHookForgeEvent(event);
            if (bukkitHookForgeEvent.getHandlers().getRegisteredListeners().length > 0) {
                org.bukkit.Bukkit.getPluginManager().callEvent(bukkitHookForgeEvent);
            }
        }
        return super.post(event);
    }
}
