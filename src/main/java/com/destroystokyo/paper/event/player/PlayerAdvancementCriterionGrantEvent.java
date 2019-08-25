package com.destroystokyo.paper.event.player;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called when a player is granted a criteria in an advancement.
 */
public class PlayerAdvancementCriterionGrantEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Advancement advancement;
    private final String criterion;
    private boolean cancel = false;

    public PlayerAdvancementCriterionGrantEvent(Player who, Advancement advancement, String criterion) {
        super(who);
        this.advancement = advancement;
        this.criterion = criterion;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the advancement which has been affected.
     *
     * @return affected advancement
     */
    public Advancement getAdvancement() {
        return advancement;
    }

    /**
     * Get the criterion which has been granted.
     *
     * @return granted criterion
     */
    public String getCriterion() {
        return criterion;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
