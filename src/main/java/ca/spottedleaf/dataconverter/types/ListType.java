package ca.spottedleaf.dataconverter.types;

public interface ListType {

    public TypeUtil getTypeUtil();

    @Override
    public int hashCode();

    @Override
    public boolean equals(final Object other);

    // Provides a deep copy of this list
    public ListType copy();

    // returns NONE if no type has been assigned. if NONE, then this list is also empty. It is not true on the other hand that an empty list has no type.
    public ObjectType getType();

    public int size();

    public void remove(final int index);

    public default Object getGeneric(final int index) {
        switch (this.getType()) {
            case NONE:
                throw new IllegalStateException("List is empty and has no type");
            case BYTE:
                return Byte.valueOf(this.getByte(index));
            case SHORT:
                return Short.valueOf(this.getShort(index));
            case INT:
                return Integer.valueOf(this.getInt(index));
            case LONG:
                return Long.valueOf(this.getLong(index));
            case FLOAT:
                return Float.valueOf(this.getFloat(index));
            case DOUBLE:
                return Double.valueOf(this.getDouble(index));
            case NUMBER:
                return this.getNumber(index);
            case BYTE_ARRAY:
                return this.getBytes(index);
            case SHORT_ARRAY:
                return this.getShorts(index);
            case INT_ARRAY:
                return this.getInts(index);
            case LONG_ARRAY:
                return this.getLongs(index);
            case LIST:
                return this.getList(index);
            case MAP:
                return this.getMap(index);
            case STRING:
                return this.getString(index);
            default:
                throw new UnsupportedOperationException(this.getType().name());
        }
    }

    public default void setGeneric(final int index, final Object to) {
        if (to instanceof Number) {
            if (to instanceof Byte) {
                this.setByte(index, ((Byte)to).byteValue());
                return;
            } else if (to instanceof Short) {
                this.setShort(index, ((Short)to).shortValue());
                return;
            } else if (to instanceof Integer) {
                this.setInt(index, ((Integer)to).intValue());
                return;
            } else if (to instanceof Long) {
                this.setLong(index, ((Long)to).longValue());
                return;
            } else if (to instanceof Float) {
                this.setFloat(index, ((Float)to).floatValue());
                return;
            } else if (to instanceof Double) {
                this.setDouble(index, ((Double)to).doubleValue());
                return;
            } // else fall through to throw
        } else if (to instanceof MapType) {
            this.setMap(index, (MapType<?>)to);
            return;
        } else if (to instanceof ListType) {
            this.setList(index, (ListType)to);
            return;
        } else if (to instanceof String) {
            this.setString(index, (String)to);
            return;
        } else if (to.getClass().isArray()) {
            if (to instanceof byte[]) {
                this.setBytes(index, (byte[])to);
                return;
            } else if (to instanceof short[]) {
                this.setShorts(index, (short[])to);
                return;
            } else if (to instanceof int[]) {
                this.setInts(index, (int[])to);
                return;
            } else if (to instanceof long[]) {
                this.setLongs(index, (long[])to);
                return;
            } // else fall through to throw
        }

        throw new IllegalArgumentException("Object " + to + " is not a valid type!");
    }

    // types here are strict. if the type on get does not match the underlying type, will throw.

    public Number getNumber(final int index);

    // if the value at index is a Number but not a byte, then returns the number casted to byte. If the value at the index is not a number, then throws
    public byte getByte(final int index);

    public void setByte(final int index, final byte to);

    // if the value at index is a Number but not a short, then returns the number casted to short. If the value at the index is not a number, then throws
    public short getShort(final int index);

    public void setShort(final int index, final short to);

    // if the value at index is a Number but not a int, then returns the number casted to int. If the value at the index is not a number, then throws
    public int getInt(final int index);

    public void setInt(final int index, final int to);

    // if the value at index is a Number but not a long, then returns the number casted to long. If the value at the index is not a number, then throws
    public long getLong(final int index);

    public void setLong(final int index, final long to);

    // if the value at index is a Number but not a float, then returns the number casted to float. If the value at the index is not a number, then throws
    public float getFloat(final int index);

    public void setFloat(final int index, final float to);

    // if the value at index is a Number but not a double, then returns the number casted to double. If the value at the index is not a number, then throws
    public double getDouble(final int index);

    public void setDouble(final int index, final double to);

    public byte[] getBytes(final int index);

    public void setBytes(final int index, final byte[] to);

    public short[] getShorts(final int index);

    public void setShorts(final int index, final short[] to);

    public int[] getInts(final int index);

    public void setInts(final int index, final int[] to);

    public long[] getLongs(final int index);

    public void setLongs(final int index, final long[] to);

    public ListType getList(final int index);

    public void setList(final int index, final ListType list);

    public <T> MapType<T> getMap(final int index);

    public void setMap(final int index, final MapType<?> to);

    public String getString(final int index);

    public void setString(final int index, final String to);

    public default void addGeneric(final Object to) {
        if (to instanceof Number) {
            if (to instanceof Byte) {
                this.addByte(((Byte)to).byteValue());
                return;
            } else if (to instanceof Short) {
                this.addShort(((Short)to).shortValue());
                return;
            } else if (to instanceof Integer) {
                this.addInt(((Integer)to).intValue());
                return;
            } else if (to instanceof Long) {
                this.addLong(((Long)to).longValue());
                return;
            } else if (to instanceof Float) {
                this.addFloat(((Float)to).floatValue());
                return;
            } else if (to instanceof Double) {
                this.addDouble(((Double)to).doubleValue());
                return;
            } // else fall through to throw
        } else if (to instanceof MapType) {
            this.addMap((MapType<?>)to);
            return;
        } else if (to instanceof ListType) {
            this.addList((ListType)to);
            return;
        } else if (to instanceof String) {
            this.addString((String)to);
            return;
        } else if (to.getClass().isArray()) {
            if (to instanceof byte[]) {
                this.addByteArray((byte[])to);
                return;
            } else if (to instanceof short[]) {
                this.addShortArray((short[])to);
                return;
            } else if (to instanceof int[]) {
                this.addIntArray((int[])to);
                return;
            } else if (to instanceof long[]) {
                this.addLongArray((long[])to);
                return;
            } // else fall through to throw
        }

        throw new IllegalArgumentException("Object " + to + " is not a valid type!");
    }

    public void addByte(final byte b);

    public void addByte(final int index, final byte b);

    public void addShort(final short s);

    public void addShort(final int index, final short s);

    public void addInt(final int i);

    public void addInt(final int index, final int i);

    public void addLong(final long l);

    public void addLong(final int index, final long l);

    public void addFloat(final float f);

    public void addFloat(final int index, final float f);

    public void addDouble(final double d);

    public void addDouble(final int index, final double d);

    public void addByteArray(final byte[] arr);

    public void addByteArray(final int index, final byte[] arr);

    public void addShortArray(final short[] arr);

    public void addShortArray(final int index, final short[] arr);

    public void addIntArray(final int[] arr);

    public void addIntArray(final int index, final int[] arr);

    public void addLongArray(final long[] arr);

    public void addLongArray(final int index, final long[] arr);

    public void addList(final ListType list);

    public void addList(final int index, final ListType list);

    public void addMap(final MapType<?> map);

    public void addMap(final int index, final MapType<?> map);

    public void addString(final String string);

    public void addString(final int index, final String string);

}
