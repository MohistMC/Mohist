package org.bukkit.map;

import org.bukkit.World;

import java.util.List;

/**
 * Represents a map item.
 */
public interface MapView {

    /**
     * Get the ID of this map item. Corresponds to the damage value of a map
     * in an inventory.
     *
     * @return The ID of the map.
     * @deprecated Magic value
     */
    @Deprecated
    short getId();

    /**
     * Check whether this map is virtual. A map is virtual if its lowermost
     * MapRenderer is plugin-provided.
     *
     * @return Whether the map is virtual.
     */
    boolean isVirtual();

    /**
     * Get the scale of this map.
     *
     * @return The scale of the map.
     */
    Scale getScale();

    /**
     * Set the scale of this map.
     *
     * @param scale The scale to set.
     */
    void setScale(Scale scale);

    /**
     * Get the center X position of this map.
     *
     * @return The center X position.
     */
    int getCenterX();

    /**
     * Set the center X position of this map.
     *
     * @param x The center X position.
     */
    void setCenterX(int x);

    /**
     * Get the center Z position of this map.
     *
     * @return The center Z position.
     */
    int getCenterZ();

    /**
     * Set the center Z position of this map.
     *
     * @param z The center Z position.
     */
    void setCenterZ(int z);

    /**
     * Get the world that this map is associated with. Primarily used by the
     * internal renderer, but may be used by external renderers. May return
     * null if the world the map is associated with is not loaded.
     *
     * @return The World this map is associated with.
     */
    World getWorld();

    /**
     * Set the world that this map is associated with. The world is used by
     * the internal renderer, and may also be used by external renderers.
     *
     * @param world The World to associate this map with.
     */
    void setWorld(World world);

    /**
     * Get a list of MapRenderers currently in effect.
     *
     * @return A {@code List<MapRenderer>} containing each map renderer.
     */
    List<MapRenderer> getRenderers();

    /**
     * Add a renderer to this map.
     *
     * @param renderer The MapRenderer to add.
     */
    void addRenderer(MapRenderer renderer);

    /**
     * Remove a renderer from this map.
     *
     * @param renderer The MapRenderer to remove.
     * @return True if the renderer was successfully removed.
     */
    boolean removeRenderer(MapRenderer renderer);

    /**
     * Whether the map will show a smaller position cursor (true), or no
     * position cursor (false) when cursor is outside of map's range.
     *
     * @return unlimited tracking state
     */
    boolean isUnlimitedTracking();

    /**
     * Whether the map will show a smaller position cursor (true), or no
     * position cursor (false) when cursor is outside of map's range.
     *
     * @param unlimited tracking state
     */
    void setUnlimitedTracking(boolean unlimited);

    /**
     * An enum representing all possible scales a map can be set to.
     */
    enum Scale {
        CLOSEST(0),
        CLOSE(1),
        NORMAL(2),
        FAR(3),
        FARTHEST(4);

        private final byte value;

        Scale(int value) {
            this.value = (byte) value;
        }

        /**
         * Get the scale given the raw value.
         *
         * @param value The raw scale
         * @return The enum scale, or null for an invalid input
         * @deprecated Magic value
         */
        @Deprecated
        public static Scale valueOf(byte value) {
            switch (value) {
                case 0:
                    return CLOSEST;
                case 1:
                    return CLOSE;
                case 2:
                    return NORMAL;
                case 3:
                    return FAR;
                case 4:
                    return FARTHEST;
                default:
                    return null;
            }
        }

        /**
         * Get the raw value of this scale level.
         *
         * @return The scale value
         * @deprecated Magic value
         */
        @Deprecated
        public byte getValue() {
            return value;
        }
    }
}
