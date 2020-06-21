package red.mohist.api.event.server;

import net.minecraftforge.fml.common.event.FMLEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitServerStartDoneEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public BukkitServerStartDoneEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
