package io.papermc.paper.event.entity;

import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Is called when an {@link org.bukkit.entity.ElderGuardian} appears in front of a {@link org.bukkit.entity.Player}.
 */
public class ElderGuardianAppearanceEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final Player affectedPlayer;

    public ElderGuardianAppearanceEvent(@NotNull Entity what, @NotNull Player affectedPlayer) {
        super(what);
        this.affectedPlayer = affectedPlayer;
    }

    /**
     * Get the player affected by the guardian appearance.
     *
     * @return Player affected by the appearance
     */
    @NotNull
    public Player getAffectedPlayer() {
        return affectedPlayer;
    }

    /**
     * The elder guardian playing the effect.
     *
     * @return The elder guardian
     */
    @NotNull
    public ElderGuardian getEntity() {
        return (ElderGuardian) entity;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
