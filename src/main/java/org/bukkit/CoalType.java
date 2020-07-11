package org.bukkit;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Represents the two types of coal
 */
public enum CoalType {
    COAL(0x0),
    CHARCOAL(0x1);

    private final static Map<Byte, CoalType> BY_DATA = Maps.newHashMap();

    static {
        for (CoalType type : values()) {
            BY_DATA.put(type.data, type);
        }
    }

    private final byte data;

    CoalType(final int data) {
        this.data = (byte) data;
    }

    /**
     * Gets the type of coal with the given data value
     *
     * @param data Data value to fetch
     * @return The {@link CoalType} representing the given value, or null if
     * it doesn't exist
     * @deprecated Magic value
     */

    public static CoalType getByData(final byte data) {
        return BY_DATA.get(data);
    }

    /**
     * Gets the associated data value representing this type of coal
     *
     * @return A byte containing the data value of this coal type
     * @deprecated Magic value
     */

    public byte getData() {
        return data;
    }
}
