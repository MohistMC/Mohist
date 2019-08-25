package red.mohist.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Deprecated
@Cancelable
public class ForgeHookBukkitEvent extends Event {
    private final org.bukkit.event.Event event;

    public ForgeHookBukkitEvent(org.bukkit.event.Event event) {
        this.event = event;
    }

    public org.bukkit.event.Event getEvent() {
        return this.event;
    }
}