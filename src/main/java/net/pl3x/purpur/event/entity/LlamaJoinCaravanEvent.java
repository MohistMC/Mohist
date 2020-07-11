package net.pl3x.purpur.event.entity;

import org.bukkit.entity.Llama;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a Llama tries to join a caravan.
 * <p>
 * Cancelling the event will not let the Llama join. To prevent future attempts
 * at joining a caravan use {@link Llama#setShouldJoinCaravan(boolean)}.
 */
public class LlamaJoinCaravanEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Llama head;
    private boolean canceled;

    public LlamaJoinCaravanEvent(@NotNull Llama llama, @NotNull Llama head) {
        super(llama);
        this.head = head;
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

    /**
     * Get the Llama that this Llama is about to follow
     *
     * @return Llama about to be followed
     */
    @NotNull
    public Llama getHead() {
        return head;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}