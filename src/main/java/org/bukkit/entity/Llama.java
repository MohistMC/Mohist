package org.bukkit.entity;

import org.bukkit.inventory.LlamaInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Llama.
 */
public interface Llama extends ChestedHorse {

    /**
     * Gets the llama's color.
     *
     * @return a {@link Color} representing the llama's color
     */
    Color getColor();

    /**
     * Sets the llama's color.
     *
     * @param color a {@link Color} for this llama
     */
    void setColor(Color color);

    /**
     * Gets the llama's strength. A higher strength llama will have more
     * inventory slots and be more threatening to entities.
     *
     * @return llama strength [1,5]
     */
    int getStrength();

    /**
     * Sets the llama's strength. A higher strength llama will have more
     * inventory slots and be more threatening to entities. Inventory slots are
     * equal to strength * 3.
     *
     * @param strength llama strength [1,5]
     */
    void setStrength(int strength);

    @Override
    LlamaInventory getInventory();

    // Purpur start

    /**
     * Check if this Llama should attempt to join a caravan
     *
     * @return True if Llama is allowed to join a caravan
     */
    boolean shouldJoinCaravan();

    /**
     *  Set if this Llama should attempt to join a caravan
     *
     *  @param shouldJoinCaravan True to allow joining a caravan
     */
    void setShouldJoinCaravan(boolean shouldJoinCaravan);

    /**
     *  Check if Llama is in a caravan
     *
     *  @return True if in caravan
     */
    boolean inCaravan();

    /**
     * Join a caravan
     *
     * @param llama Head of caravan to join
     */
    void joinCaravan(@NotNull Llama llama);

    /**
     *  Leave current caravan if in one
     */
    void leaveCaravan();

    /**
     * Check if another Llama is following this Llama
     *
     * @return True if being followed in the caravan
     */
    boolean hasCaravanTail();

    /**
     *  Get the Llama that this Llama is following
     *  <p>
     *  Does not necessarily mean the leader of the entire caravan
     *
     *  @return The Llama being followed
     */
    @Nullable
    Llama getCaravanHead();

    /**
     * Get the Llama following this Llama, if any
     *
     * @return The Llama following this one
     */
    @Nullable
    Llama getCaravanTail();
    // Purpur end

    /**
     * Represents the base color that the llama has.
     */
    public enum Color {

        /**
         * A cream-colored llama.
         */
        CREAMY,
        /**
         * A white llama.
         */
        WHITE,
        /**
         * A brown llama.
         */
        BROWN,
        /**
         * A gray llama.
         */
        GRAY;
    }

}
