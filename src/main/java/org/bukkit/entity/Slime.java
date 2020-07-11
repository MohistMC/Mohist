package org.bukkit.entity;

/**
 * Represents a Slime.
 */
public interface Slime extends LivingEntity {

    /**
     * @return The size of the slime
     */
    int getSize();

    /**
     * @param sz The new size of the slime.
     */
    void setSize(int sz);

    /**
     * Get the {@link LivingEntity} this slime is currently targeting.
     *
     * @return the current target, or null if no target exists.
     */
    LivingEntity getTarget();

    /**
     * Set the {@link LivingEntity} target for this slime. Set to null to clear
     * the target.
     *
     * @param target the entity to target
     */
    void setTarget(LivingEntity target);
}
