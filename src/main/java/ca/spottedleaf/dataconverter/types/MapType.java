package ca.spottedleaf.dataconverter.types;

import java.util.Set;

public interface MapType<K> {

    public TypeUtil getTypeUtil();

    @Override
    public int hashCode();

    @Override
    public boolean equals(final Object other);

    public int size();

    public boolean isEmpty();

    public void clear();

    public Set<K> keys();

    // Provides a deep copy of this map
    public MapType<K> copy();

    public boolean hasKey(final K key);

    public boolean hasKey(final K key, final ObjectType type);

    public void remove(final K key);

    public Object getGeneric(final K key);

    // types here are not strict. if the key maps to a different type, default is always returned
    // if default is not a parameter, then default is always null

    public Number getNumber(final K key);

    public Number getNumber(final K key, final Number dfl);

    public boolean getBoolean(final K key);

    public boolean getBoolean(final K key, final boolean dfl);

    public void setBoolean(final K key, final boolean val);

    // if the mapped value is a Number but not a byte, then the number is casted to byte. If the mapped value does not exist or is not a number, returns 0
    public byte getByte(final K key);

    // if the mapped value is a Number but not a byte, then the number is casted to byte. If the mapped value does not exist or is not a number, returns dfl
    public byte getByte(final K key, final byte dfl);

    public void setByte(final K key, final byte val);

    // if the mapped value is a Number but not a short, then the number is casted to short. If the mapped value does not exist or is not a number, returns 0
    public short getShort(final K key);

    // if the mapped value is a Number but not a short, then the number is casted to short. If the mapped value does not exist or is not a number, returns dfl
    public short getShort(final K key, final short dfl);

    public void setShort(final K key, final short val);

    // if the mapped value is a Number but not a int, then the number is casted to int. If the mapped value does not exist or is not a number, returns 0
    public int getInt(final K key);

    // if the mapped value is a Number but not a int, then the number is casted to int. If the mapped value does not exist or is not a number, returns dfl
    public int getInt(final K key, final int dfl);

    public void setInt(final K key, final int val);

    // if the mapped value is a Number but not a long, then the number is casted to long. If the mapped value does not exist or is not a number, returns 0
    public long getLong(final K key);

    // if the mapped value is a Number but not a long, then the number is casted to long. If the mapped value does not exist or is not a number, returns dfl
    public long getLong(final K key, final long dfl);

    public void setLong(final K key, final long val);

    // if the mapped value is a Number but not a float, then the number is casted to float. If the mapped value does not exist or is not a number, returns 0
    public float getFloat(final K key);

    // if the mapped value is a Number but not a float, then the number is casted to float. If the mapped value does not exist or is not a number, returns dfl
    public float getFloat(final K key, final float dfl);

    public void setFloat(final K key, final float val);

    // if the mapped value is a Number but not a double, then the number is casted to double. If the mapped value does not exist or is not a number, returns 0
    public double getDouble(final K key);

    // if the mapped value is a Number but not a double, then the number is casted to double. If the mapped value does not exist or is not a number, returns dfl
    public double getDouble(final K key, final double dfl);

    public void setDouble(final K key, final double val);

    public byte[] getBytes(final K key);

    public byte[] getBytes(final K key, final byte[] dfl);

    public void setBytes(final K key, final byte[] val);

    public short[] getShorts(final K key);

    public short[] getShorts(final K key, final short[] dfl);

    public void setShorts(final K key, final short[] val);

    public int[] getInts(final K key);

    public int[] getInts(final K key, final int[] dfl);

    public void setInts(final K key, final int[] val);

    public long[] getLongs(final K key);

    public long[] getLongs(final K key, final long[] dfl);

    public void setLongs(final K key, final long[] val);

    public ListType getListUnchecked(final K key);

    public ListType getListUnchecked(final K key, final ListType dfl);

    public default ListType getList(final K key, final ObjectType type) {
        return this.getList(key, type, null);
    }

    public default ListType getOrCreateList(final K key, final ObjectType type) {
        ListType ret = this.getList(key, type);
        if (ret == null) {
            this.setList(key, ret = this.getTypeUtil().createEmptyList());
        }

        return ret;
    }

    public default ListType getList(final K key, final ObjectType type, final ListType dfl) {
        final ListType ret = this.getListUnchecked(key, null);
        final ObjectType retType;
        if (ret != null && ((retType = ret.getType()) == type || retType == ObjectType.UNDEFINED)) {
            return ret;
        } else {
            return dfl;
        }
    }

    public void setList(final K key, final ListType val);

    public <T> MapType<T> getMap(final K key);

    public default <T> MapType<T> getOrCreateMap(final K key) {
        MapType<T> ret = this.getMap(key);
        if (ret == null) {
            this.setMap(key, ret = this.getTypeUtil().createEmptyMap());
        }

        return ret;
    }

    public <T> MapType<T> getMap(final K key, final MapType<T> dfl);

    public void setMap(final K key, final MapType<?> val);

    public String getString(final K key);

    public String getString(final K key, final String dfl);

    public void setString(final K key, final String val);

    public default void setGeneric(final K key, final Object value) {
        if (value instanceof Boolean) {
            this.setBoolean(key, ((Boolean)value).booleanValue());
        } else if (value instanceof Number) {
            if (value instanceof Byte) {
                this.setByte(key, ((Byte)value).byteValue());
                return;
            } else if (value instanceof Short) {
                this.setShort(key, ((Short)value).shortValue());
                return;
            } else if (value instanceof Integer) {
                this.setInt(key, ((Integer)value).intValue());
                return;
            } else if (value instanceof Long) {
                this.setLong(key, ((Long)value).longValue());
                return;
            } else if (value instanceof Float) {
                this.setFloat(key, ((Float)value).floatValue());
                return;
            } else if (value instanceof Double) {
                this.setDouble(key, ((Double)value).doubleValue());
                return;
            } // else fall through to throw
        } else if (value instanceof MapType) {
            this.setMap(key, (MapType<?>)value);
            return;
        } else if (value instanceof ListType) {
            this.setList(key, (ListType)value);
            return;
        } else if (value instanceof String) {
            this.setString(key, (String)value);
            return;
        } else if (value.getClass().isArray()) {
            if (value instanceof byte[]) {
                this.setBytes(key, (byte[])value);
                return;
            } else if (value instanceof short[]) {
                this.setShorts(key, (short[])value);
                return;
            } else if (value instanceof int[]) {
                this.setInts(key, (int[])value);
                return;
            } else if (value instanceof long[]) {
                this.setLongs(key, (long[])value);
                return;
            } // else fall through to throw
        }

        throw new IllegalArgumentException("Object " + value + " is not a valid type!");
    }
}
