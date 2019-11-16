package thermos.event;
/**
 * Add new event from PaperSpigot, BeaconEffectEvent.
 * Called when an effect is being applied to a player.
 */

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.potion.PotionEffect;

public class BeaconEffectEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private PotionEffect effect;
    private Player player;
    private boolean primary;

    public BeaconEffectEvent(Block block, PotionEffect effect, Player player, boolean primary) {
        super(block);
        this.effect = effect;
        this.player = player;
        this.primary = primary;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = true;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    /**
     * Gets the effect being applied.
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


}
