package io.papermc.paper.event.player;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player trades with a villager or wandering trader
 */
public class PlayerTradeEvent extends PlayerPurchaseEvent {

    private final AbstractVillager villager;

    public PlayerTradeEvent(@NotNull Player player, @NotNull AbstractVillager villager, @NotNull MerchantRecipe trade, boolean rewardExp, boolean increaseTradeUses) {
        super(player, trade, rewardExp, increaseTradeUses);
        this.villager = villager;
    }

    /**
     * Gets the Villager or Wandering trader associated with this event
     * @return the villager or wandering trader
     */
    @NotNull
    public AbstractVillager getVillager() {
        return this.villager;
    }

}
