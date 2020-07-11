package org.bukkit.entity;

import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Mob. Mobs are living entities with simple AI.
 */
public interface Mob extends LivingEntity, Lootable {

    /**
     * Gets the current target of this Mob
     *
     * @return Current target of this creature, or null if none exists
     */
    @Nullable LivingEntity getTarget();

    /**
     * Instructs this Mob to set the specified LivingEntity as its target.
     * <p>
     * Hostile creatures may attack their target, and friendly creatures may
     * follow their target.
     *
     * @param target New LivingEntity to target, or null to clear the target
     */
    void setTarget(@Nullable LivingEntity target);

    /**
     * Gets whether this mob is aware of its surroundings.
     * <p>
     * Unaware mobs will still move if pushed, attacked, etc. but will not move
     * or perform any actions on their own. Unaware mobs may also have other
     * unspecified behaviours disabled, such as drowning.
     *
     * @return whether the mob is aware
     */
    boolean isAware();

    /**
     * Sets whether this mob is aware of its surroundings.
     * <p>
     * Unaware mobs will still move if pushed, attacked, etc. but will not move
     * or perform any actions on their own. Unaware mobs may also have other
     * unspecified behaviours disabled, such as drowning.
     *
     * @param aware whether the mob is aware
     */
    void setAware(boolean aware);
}
