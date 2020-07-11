package org.bukkit.entity;

/**
 * Represents a Parrot.
 */
public interface Parrot extends Animals, Tameable, Sittable {

    /**
     * Get the variant of this parrot.
     *
     * @return parrot variant
     */
    Variant getVariant();

    /**
     * Set the variant of this parrot.
     *
     * @param variant parrot variant
     */
    void setVariant(Variant variant);

    /**
     * Represents the variant of a parrot - ie its color.
     */
    enum Variant {
        /**
         * Classic parrot - red with colored wingtips.
         */
        RED,
        /**
         * Royal blue colored parrot.
         */
        BLUE,
        /**
         * Green colored parrot.
         */
        GREEN,
        /**
         * Cyan colored parrot.
         */
        CYAN,
        /**
         * Gray colored parrot.
         */
        GRAY
    }
}
