package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;

public interface MetadataValue {

    /**
     * Fetches the value of this metadata item.
     *
     * @return the metadata value.
     */
    Object value();

    /**
     * Attempts to convert the value of this metadata item into an int.
     *
     * @return the value as an int.
     */
    int asInt();

    /**
     * Attempts to convert the value of this metadata item into a float.
     *
     * @return the value as a float.
     */
    float asFloat();

    /**
     * Attempts to convert the value of this metadata item into a double.
     *
     * @return the value as a double.
     */
    double asDouble();

    /**
     * Attempts to convert the value of this metadata item into a long.
     *
     * @return the value as a long.
     */
    long asLong();

    /**
     * Attempts to convert the value of this metadata item into a short.
     *
     * @return the value as a short.
     */
    short asShort();

    /**
     * Attempts to convert the value of this metadata item into a byte.
     *
     * @return the value as a byte.
     */
    byte asByte();

    /**
     * Attempts to convert the value of this metadata item into a boolean.
     *
     * @return the value as a boolean.
     */
    boolean asBoolean();

    /**
     * Attempts to convert the value of this metadata item into a string.
     *
     * @return the value as a string.
     */
    String asString();

    /**
     * Returns the {@link Plugin} that created this metadata item.
     *
     * @return the plugin that owns this metadata value. This should never be
     * null.
     */
    Plugin getOwningPlugin();

    /**
     * Invalidates this metadata item, forcing it to recompute when next
     * accessed.
     */
    void invalidate();
}
