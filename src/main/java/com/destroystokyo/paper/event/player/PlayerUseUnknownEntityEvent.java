package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerUseUnknownEntityEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final int entityId;
    private final boolean attack;
    private final EquipmentSlot hand;

    public PlayerUseUnknownEntityEvent(Player who, int entityId, boolean attack, EquipmentSlot hand) {
        super(who);
        this.entityId = entityId;
        this.attack = attack;
        this.hand = hand;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean isAttack() {
        return this.attack;
    }

    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
