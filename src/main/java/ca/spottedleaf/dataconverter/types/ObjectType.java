package ca.spottedleaf.dataconverter.types;

public enum ObjectType {
    NONE(null),
    BYTE(Byte.class),
    SHORT(Short.class),
    INT(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    NUMBER(Number.class),
    BYTE_ARRAY(byte[].class),
    SHORT_ARRAY(short[].class),
    INT_ARRAY(int[].class),
    LONG_ARRAY(long[].class),
    LIST(ListType.class),
    MAP(MapType.class),
    STRING(String.class),
    UNDEFINED(null);

    private final Class<?> clazz;
    private final boolean isNumber;

    private ObjectType(final Class<?> clazz) {
        this.clazz = clazz;
        this.isNumber = clazz != null && Number.class.isAssignableFrom(clazz);
    }

    public boolean isNumber() {
        return this.isNumber;
    }

    public Class<?> getObjectClass() {
        return this.clazz;
    }

    public static ObjectType getType(final Object object) {
        if (object instanceof Number) {
            if (object instanceof Byte) {
                return BYTE;
            } else if (object instanceof Short) {
                return SHORT;
            } else if (object instanceof Integer) {
                return INT;
            } else if (object instanceof Long) {
                return LONG;
            } else if (object instanceof Float) {
                return FLOAT;
            } else if (object instanceof Double) {
                return DOUBLE;
            } // else return null
        } else if (object instanceof MapType) {
            return MAP;
        } else if (object instanceof ListType) {
            return LIST;
        } else if (object instanceof String) {
            return STRING;
        } else if (object.getClass().isArray()) {
            if (object instanceof byte[]) {
                return BYTE_ARRAY;
            } else if (object instanceof short[]) {
                return SHORT_ARRAY;
            } else if (object instanceof int[]) {
                return INT_ARRAY;
            } else if (object instanceof long[]) {
                return LONG_ARRAY;
            } // else return null
        }

        return null;
    }
}
