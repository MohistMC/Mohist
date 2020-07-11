package com.destroystokyo.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.potion.PotionEffect;

/**
 * Called when a beacon effect is being applied to a player.
 */
public class BeaconEffectEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final boolean primary;
    private boolean cancelled;
    private PotionEffect effect;

    public BeaconEffectEvent(Block block, PotionEffect effect, Player player, boolean primary) {
        super(block);
        this.effect = effect;
        this.player = player;
        this.primary = primary;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the potion effect being applied.
     *
     * @return Potion effect
     */
    public PotionEffect getEffect() {
        return effect;
    }

    /**
     * Sets the potion effect that will be applied.
     *
     * @param effect Potion effect
     */
    public void setEffect(PotionEffect effect) {
        this.effect = effect;
    }

    /**
     * Gets the player who the potion effect is being applied to.
     *
     * @return Affected player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets whether the effect is a primary beacon effect.
     *
     * @return true if this event represents a primary effect
     */
    public boolean isPrimary() {
        return primary;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
