package net.pl3x.purpur.event.entity;

import org.bukkit.entity.Llama;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a Llama leaves a caravan
 */
public class LlamaLeaveCaravanEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();

    public LlamaLeaveCaravanEvent(@NotNull Llama llama) {
        super(llama);
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @NotNull
    public Llama getEntity() {
        return (Llama) entity;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
