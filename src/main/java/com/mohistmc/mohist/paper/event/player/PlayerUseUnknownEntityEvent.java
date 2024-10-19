package com.mohistmc.mohist.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an event that is called when a player right-clicks an unknown entity.
 * Useful for plugins dealing with virtual entities (entities that don't actually spawned on the server).
 * <br>
 * This event may be called multiple times per interaction with different interaction hands
 * and with or without the clicked position.
 */
public class PlayerUseUnknownEntityEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final int entityId;
    private final boolean attack;
    private final @NotNull EquipmentSlot hand;
    private final @Nullable Vector clickedPosition;

    public PlayerUseUnknownEntityEvent(@NotNull Player who, int entityId, boolean attack, @NotNull EquipmentSlot hand, @Nullable Vector clickedPosition) {
        super(who);
        this.entityId = entityId;
        this.attack = attack;
        this.hand = hand;
        this.clickedPosition = clickedPosition;
    }

    /**
     * Returns the entity id of the unknown entity that was interacted with.
     *
     * @return the entity id of the entity that was interacted with
     */
    public int getEntityId() {
        return this.entityId;
    }

    /**
     * Returns whether the interaction was an attack.
     *
     * @return true if the player is attacking the entity, false if the player is interacting with the entity
     */
    public boolean isAttack() {
        return this.attack;
    }

    /**
     * Returns the hand used to perform this interaction.
     *
     * @return the hand used to interact
     */
    public @NotNull EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Returns the position relative to the entity that was clicked, or null if not available.
     * See {@link org.bukkit.event.player.PlayerInteractAtEntityEvent} for more details.
     *
     * @return the position relative to the entity that was clicked, or null if not available
     * @see org.bukkit.event.player.PlayerInteractAtEntityEvent
     */
    public @Nullable Vector getClickedRelativePosition() {
        return clickedPosition.clone();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
