package red.mohist.forge.event;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventListener;
import org.bukkit.event.Cancellable;

public class ForgeToBukkitEventData {

    private final Event forgeEvent;
    private final IEventListener[] forgeListeners;
    private org.bukkit.event.Event bukkitEvent;
    private boolean beforeModifications = false;
    private boolean forced = false;

    public ForgeToBukkitEventData(Event forgeEvent, final IEventListener[] forgeListeners) {
        this.forgeEvent = forgeEvent;
        this.forgeListeners = forgeListeners;
    }

    public Event getForgeEvent() {
        return this.forgeEvent;
    }

    public org.bukkit.event.Event getBukkitEvent() {
        return this.bukkitEvent;
    }

    public IEventListener[] getForgeListeners() {
        return this.forgeListeners;
    }

    public boolean isBeforeModifications() {
        return this.beforeModifications;
    }

    public boolean isForced() {
        return this.forced;
    }

    public void setBeforeModifications(boolean beforeModifications) {
        this.beforeModifications = beforeModifications;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public void setBukkitEvent(org.bukkit.event.Event event) {
        this.bukkitEvent = event;
    }

    public void propagateCancelled() {
        // Only propagate if Forge Event wasn't cancelled already
        if (this.bukkitEvent instanceof Cancellable && this.forgeEvent.isCancelable() && !this.forgeEvent.isCanceled()) {
            this.forgeEvent.setCanceled(((Cancellable) this.bukkitEvent).isCancelled());
        }
    }
}
