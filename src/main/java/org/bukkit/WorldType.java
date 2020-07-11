package org.bukkit;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Represents various types of worlds that may exist
 */
public enum WorldType {
    NORMAL("DEFAULT"),
    FLAT("FLAT"),
    VERSION_1_1("DEFAULT_1_1"),
    LARGE_BIOMES("LARGEBIOMES"),
    AMPLIFIED("AMPLIFIED"),
    CUSTOMIZED("CUSTOMIZED");

    public static Map<String, WorldType> BY_NAME = Maps.newHashMap();

    static {
        for (WorldType type : values()) {
            BY_NAME.put(type.name, type);
        }
    }

    private final String name;

    WorldType(String name) {
        this.name = name;
    }

    /**
     * Gets a Worldtype by its name
     *
     * @param name Name of the WorldType to get
     * @return Requested WorldType, or null if not found
     */
    public static WorldType getByName(String name) {
        return BY_NAME.get(name.toUpperCase(java.util.Locale.ENGLISH));
    }

    /**
     * Gets the name of this WorldType
     *
     * @return Name of this type
     */
    public String getName() {
        return name;
    }
}
