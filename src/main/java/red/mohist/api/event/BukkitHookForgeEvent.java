package red.mohist.api.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BukkitHookForgeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Event event;

    public BukkitHookForgeEvent(Event event) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.event = event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return Forge and Mods Event
     */
    public Event getEvent() {
        return this.event;
    }

    @Override
    public String getEventName() {
        return event.getClass().getSimpleName();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}