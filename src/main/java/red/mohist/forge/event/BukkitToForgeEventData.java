package red.mohist.forge.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class BukkitToForgeEventData {

    private final Event bukkitEvent;
    private final Class<? extends net.minecraftforge.eventbus.api.Event> forgeClass;
    private net.minecraftforge.eventbus.api.Event forgeEvent;

    public BukkitToForgeEventData(Event bukkitEvent, Class<? extends net.minecraftforge.eventbus.api.Event> forgeClass, boolean useCauseStackManager) {
        this.bukkitEvent = bukkitEvent;
        this.forgeClass = forgeClass;
    }

    public BukkitToForgeEventData(ForgeToBukkitEventData eventData) {
        this.bukkitEvent = eventData.getBukkitEvent();
        this.forgeClass = eventData.getForgeEvent().getClass();
        this.forgeEvent = eventData.getForgeEvent();
    }

    public Event getBukkitEvent() {
        return this.bukkitEvent;
    }

    public net.minecraftforge.eventbus.api.Event getForgeEvent() {
        return this.forgeEvent;
    }

    public Class<? extends net.minecraftforge.eventbus.api.Event> getForgeClass() {
        return this.forgeClass;
    }

    public void setForgeEvent(net.minecraftforge.eventbus.api.Event event) {
        this.forgeEvent = event;
    }

    public void propagateCancelled() {
        if (this.bukkitEvent instanceof Cancellable && this.forgeEvent.isCancelable() && !((Cancellable) this.bukkitEvent).isCancelled()) {
            ((Cancellable) this.bukkitEvent).setCancelled(this.forgeEvent.isCanceled());
        }
    }
}
