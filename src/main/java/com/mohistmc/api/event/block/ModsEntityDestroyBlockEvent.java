package com.mohistmc.api.event.block;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

public class ModsEntityDestroyBlockEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final Location location;
    private final Entity entity;

    public ModsEntityDestroyBlockEvent(Location location, Entity entity) {
        super(location.getBlock());
        this.location = location;
        this.entity = entity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public Location getLocation() {
        return location;
    }

    public Entity getEntity() {
        return entity;
    }
}
