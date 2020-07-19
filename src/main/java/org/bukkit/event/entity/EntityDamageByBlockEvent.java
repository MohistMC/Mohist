package org.bukkit.event.entity;

import com.google.common.base.Function;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.Map;

/**
 * Called when an entity is damaged by a block
 */
public class EntityDamageByBlockEvent extends EntityDamageEvent {
    private final Block damager;

    public EntityDamageByBlockEvent(final Block damager, final Entity damagee, final DamageCause cause, final double damage) {
        super(damagee, cause, damage);
        this.damager = damager;
    }

    public EntityDamageByBlockEvent(final Block damager, final Entity damagee, final DamageCause cause, final Map<DamageModifier, Double> modifiers, final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee, cause, modifiers, modifierFunctions);
        this.damager = damager;
    }

    /**
     * Returns the block that damaged the player.
     *
     * @return Block that damaged the player
     */
    public Block getDamager() {
        return damager;
    }
}
