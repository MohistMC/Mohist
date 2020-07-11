package red.mohist.api.event;


import net.minecraftforge.eventbus.api.Event;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

/**
 * Using Bukkit to handle Forge's Event
 * look red.mohist.test.BukkitHookForgeEventTest
 */
public class BukkitHookForgeEvent extends org.bukkit.event.Event {
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
    public HandlerList getHandlers() {
        return handlers;
    }
}
