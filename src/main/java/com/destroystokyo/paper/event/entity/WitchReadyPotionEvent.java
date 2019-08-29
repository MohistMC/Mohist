package com.destroystokyo.paper.event.entity;

import org.bukkit.Material;
import org.bukkit.entity.Witch;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

public class WitchReadyPotionEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private ItemStack potion;
    private boolean cancelled = false;

    public WitchReadyPotionEvent(Witch witch, ItemStack potion) {
        super(witch);
        this.potion = potion;
    }

    /**
     * Fires thee event, returning the desired potion, or air of cancelled
     * @param witch the witch whom is readying to use a potion
     * @param potion the potion to be used
     * @return The ItemStack to be used
     */
    public static ItemStack process(Witch witch, ItemStack potion) {
        WitchReadyPotionEvent event = new WitchReadyPotionEvent(witch, potion);
        if (!event.callEvent() || event.getPotion() == null) {
            return new ItemStack(Material.AIR);
        }
        return event.getPotion();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public Witch getEntity() {
        return (Witch) super.getEntity();
    }

    /**
     * @return the potion the witch is readying to use
     */
    public ItemStack getPotion() {
        return potion;
    }

    /**
     * Sets the potion the which is going to hold and use
     * @param potion The potion
     */
    public void setPotion(ItemStack potion) {
        this.potion = potion != null ? potion.clone() : null;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
