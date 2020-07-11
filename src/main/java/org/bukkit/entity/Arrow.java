package org.bukkit.entity;

import org.bukkit.block.Block;

/**
 * Represents an arrow.
 */
public interface Arrow extends Projectile {

    /**
     * Gets the knockback strength for an arrow, which is the
     * {@link org.bukkit.enchantments.Enchantment#KNOCKBACK KnockBack} level
     * of the bow that shot it.
     *
     * @return the knockback strength value
     */
    int getKnockbackStrength();

    /**
     * Sets the knockback strength for an arrow.
     *
     * @param knockbackStrength the knockback strength value
     */
    void setKnockbackStrength(int knockbackStrength);

    /**
     * Gets whether this arrow is critical.
     * <p>
     * Critical arrows have increased damage and cause particle effects.
     * <p>
     * Critical arrows generally occur when a player fully draws a bow before
     * firing.
     *
     * @return true if it is critical
     */
    boolean isCritical();

    /**
     * Sets whether or not this arrow should be critical.
     *
     * @param critical whether or not it should be critical
     */
    void setCritical(boolean critical);

    /**
     * Gets whether this arrow is in a block or not.
     * <p>
     * Arrows in a block are motionless and may be picked up by players.
     *
     * @return true if in a block
     */
    boolean isInBlock();

    /**
     * Gets the block to which this arrow is attached.
     *
     * @return the attached block or null if not attached
     */
    Block getAttachedBlock();

    /**
     * Gets the current pickup status of this arrow.
     *
     * @return the pickup status of this arrow.
     */
    PickupStatus getPickupStatus();

    /**
     * Sets the current pickup status of this arrow.
     *
     * @param status new pickup status of this arrow.
     */
    void setPickupStatus(PickupStatus status);

    @Override
    Spigot spigot();

    /**
     * Gets the ItemStack for this arrow.
     *
     * @return The ItemStack, as if a player picked up the arrow
     */
    org.bukkit.inventory.ItemStack getItemStack();

    /**
     * Represents the pickup status of this arrow.
     */
    enum PickupStatus {
        /**
         * The arrow cannot be picked up.
         */
        DISALLOWED,
        /**
         * The arrow can be picked up.
         */
        ALLOWED,
        /**
         * The arrow can only be picked up by players in creative mode.
         */
        CREATIVE_ONLY
    }
    // Spigot end

    // Spigot start
    class Spigot extends Entity.Spigot {

        public double getDamage() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setDamage(double damage) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
