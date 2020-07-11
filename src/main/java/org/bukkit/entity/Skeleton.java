package org.bukkit.entity;

/**
 * Represents a Skeleton.
 */
public interface Skeleton extends Monster {

    /**
     * Gets the current type of this skeleton.
     *
     * @return Current type
     * @deprecated should check what class instance this is
     */
    @Deprecated
    SkeletonType getSkeletonType();

    /**
     * @deprecated Must spawn a new subtype variant
     */
    @Deprecated
    void setSkeletonType(SkeletonType type);

    /*
     * @deprecated classes are different types
     */
    @Deprecated
    enum SkeletonType {

        /**
         * Standard skeleton type.
         */
        NORMAL,
        /**
         * Wither skeleton. Generally found in Nether fortresses.
         */
        WITHER,
        /**
         * Stray skeleton. Generally found in ice biomes. Shoots tipped arrows.
         */
        STRAY
    }
}
