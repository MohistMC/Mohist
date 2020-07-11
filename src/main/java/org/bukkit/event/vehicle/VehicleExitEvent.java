package org.bukkit.event.vehicle;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Raised when a living entity exits a vehicle.
 */
public class VehicleExitEvent extends VehicleEvent implements Cancellable {

    // EMC end
    private static final HandlerList handlers = new HandlerList();
    private final LivingEntity exited;
    public DismountReason reason = DismountReason.UNKNOWN;
    private boolean cancelled;
    public VehicleExitEvent(final Vehicle vehicle, final LivingEntity exited) {
        super(vehicle);
        this.exited = exited;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public DismountReason getReason() {
        return reason;
    }

    /**
     * Get the living entity that exited the vehicle.
     *
     * @return The entity.
     */
    public LivingEntity getExited() {
        return exited;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    // EMC start
    public enum DismountReason {
        PLAYER, WATER, DEAD, TRANSFER, UNKNOWN, DISCONNECT
    }
}
