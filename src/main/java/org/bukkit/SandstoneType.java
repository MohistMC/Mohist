package org.bukkit;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Represents the three different types of Sandstone
 */
public enum SandstoneType {
    CRACKED(0x0),
    GLYPHED(0x1),
    SMOOTH(0x2);

    private final static Map<Byte, SandstoneType> BY_DATA = Maps.newHashMap();

    static {
        for (SandstoneType type : values()) {
            BY_DATA.put(type.data, type);
        }
    }

    private final byte data;

    private SandstoneType(final int data) {
        this.data = (byte) data;
    }

    /**
     * Gets the type of sandstone with the given data value
     *
     * @param data Data value to fetch
     * @return The {@link SandstoneType} representing the given value, or null
     * if it doesn't exist
     * @deprecated Magic value
     */

    public static SandstoneType getByData(final byte data) {
        return BY_DATA.get(data);
    }

    /**
     * Gets the associated data value representing this type of sandstone
     *
     * @return A byte containing the data value of this sandstone type
     * @deprecated Magic value
     */

    public byte getData() {
        return data;
    }
}
