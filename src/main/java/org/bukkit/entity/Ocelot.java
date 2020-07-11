package org.bukkit.entity;

/**
 * A wild tameable cat
 */
public interface Ocelot extends Animals, Tameable, Sittable {

    /**
     * Gets the current type of this cat.
     *
     * @return Type of the cat.
     */
    Type getCatType();

    /**
     * Sets the current type of this cat.
     *
     * @param type New type of this cat.
     */
    void setCatType(Type type);

    /**
     * Represents the various different cat types there are.
     */
    enum Type {
        WILD_OCELOT(0),
        BLACK_CAT(1),
        RED_CAT(2),
        SIAMESE_CAT(3);

        private static final Type[] types = new Type[Type.values().length];

        static {
            for (Type type : values()) {
                types[type.getId()] = type;
            }
        }

        private final int id;

        Type(int id) {
            this.id = id;
        }

        /**
         * Gets a cat type by its ID.
         *
         * @param id ID of the cat type to get.
         * @return Resulting type, or null if not found.
         * @deprecated Magic value
         */
        @Deprecated
        public static Type getType(int id) {
            return (id >= types.length) ? null : types[id];
        }

        /**
         * Gets the ID of this cat type.
         *
         * @return Type ID.
         * @deprecated Magic value
         */
        @Deprecated
        public int getId() {
            return id;
        }
    }
}
