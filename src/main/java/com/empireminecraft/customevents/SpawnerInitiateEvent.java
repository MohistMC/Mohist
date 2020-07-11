package com.empireminecraft.customevents;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;

public class SpawnerInitiateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Location loc;
    private final World world;
    private final HumanEntity entity;
    private boolean canceled;
    private NamespacedKey mob;

    public SpawnerInitiateEvent(NamespacedKey mob, World world, Location loc, HumanEntity entity) {
        this.world = world;
        this.loc = loc;
        this.mob = mob;
        this.entity = entity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public World getWorld() {
        return this.world;
    }

    @Nullable
    public EntityType getMobType() {
        return mob != null ? EntityType.fromName(mob.getKey()) : null;
    }

    public void setMobType(NamespacedKey key) {
        this.mob = key;
    }

    public HumanEntity getTriggeringPlayer() {
        return this.entity;
    }

    public Location getSpawnerLocation() {
        return this.loc;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}