package ca.spottedleaf.dataconverter.types.json;

import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.TypeUtil;
import ca.spottedleaf.dataconverter.types.Types;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public final class JsonListType implements ListType {

    protected final JsonArray array;
    protected final boolean compressed;

    public JsonListType(final boolean compressed) {
        this.array = new JsonArray();
        this.compressed = compressed;
    }

    public JsonListType(final JsonArray array, final boolean compressed) {
        this.array = array;
        this.compressed = compressed;
    }

    @Override
    public TypeUtil getTypeUtil() {
        return this.compressed ? Types.JSON_COMPRESSED : Types.JSON;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != JsonListType.class) {
            return false;
        }

        return this.array.equals(((JsonListType)obj).array);
    }

    @Override
    public int hashCode() {
        return this.array.hashCode();
    }

    @Override
    public String toString() {
        return "JsonListType{" +
                "array=" + this.array +
                ", compressed=" + this.compressed +
                '}';
    }

    public JsonArray getJson() {
        return this.array;
    }

    @Override
    public ListType copy() {
        return new JsonListType(JsonTypeUtil.copyJson(this.array), this.compressed);
    }

    @Override
    public ObjectType getType() {
        return ObjectType.UNDEFINED;
    }

    @Override
    public int size() {
        return this.array.size();
    }

    @Override
    public void remove(final int index) {
        this.array.remove(index);
    }

    @Override
    public Number getNumber(final int index) {
        final JsonElement element = this.array.get(index);
        if (element instanceof JsonPrimitive) {
            final JsonPrimitive primitive = (JsonPrimitive)element;
            if (primitive.isNumber()) {
                return primitive.getAsNumber();
            } else if (primitive.isBoolean()) {
                return primitive.getAsBoolean() ? Byte.valueOf((byte)1) : Byte.valueOf((byte)0);
            } else if (this.compressed && primitive.isString()) {
                try {
                    return Integer.valueOf(Integer.parseInt(primitive.getAsString()));
                } catch (final NumberFormatException ex) {
                    return null;
                }
            }
        }

        return null;
    }

    @Override
    public byte getByte(final int index) {
        final Number number = this.getNumber(index);

        return number == null ? 0 : number.byteValue();
    }

    @Override
    public void setByte(final int index, final byte to) {
        this.array.set(index, new JsonPrimitive(Byte.valueOf(to)));
    }

    @Override
    public short getShort(final int index) {
        final Number number = this.getNumber(index);

        return number == null ? 0 : number.shortValue();
    }

    @Override
    public void setShort(final int index, final short to) {
        this.array.set(index, new JsonPrimitive(Short.valueOf(to)));
    }

    @Override
    public int getInt(final int index) {
        final Number number = this.getNumber(index);

        return number == null ? 0 : number.intValue();
    }

    @Override
    public void setInt(final int index, final int to) {
        this.array.set(index, new JsonPrimitive(Integer.valueOf(to)));
    }

    @Override
    public long getLong(final int index) {
        final Number number = this.getNumber(index);

        return number == null ? 0 : number.longValue();
    }

    @Override
    public void setLong(final int index, final long to) {
        this.array.set(index, new JsonPrimitive(Long.valueOf(to)));
    }

    @Override
    public float getFloat(final int index) {
        final Number number = this.getNumber(index);

        return number == null ? 0 : number.floatValue();
    }

    @Override
    public void setFloat(final int index, final float to) {
        this.array.set(index, new JsonPrimitive(Float.valueOf(to)));
    }

    @Override
    public double getDouble(final int index) {
        final Number number = this.getNumber(index);

        return number == null ? 0 : number.doubleValue();
    }

    @Override
    public void setDouble(final int index, final double to) {
        this.array.set(index, new JsonPrimitive(Double.valueOf(to)));
    }

    @Override
    public byte[] getBytes(final int index) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBytes(final int index, final byte[] to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short[] getShorts(final int index) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void setShorts(final int index, final short[] to) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] getInts(final int index) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInts(final int index, final int[] to) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getLongs(final int index) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLongs(final int index, final long[] to) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public ListType getList(final int index) {
        final JsonElement element = this.array.get(index);
        if (element instanceof JsonArray) {
            return new JsonListType((JsonArray)element, this.compressed);
        }
        return null;
    }

    @Override
    public void setList(final int index, final ListType list) {
        this.array.set(index, ((JsonListType)list).array);
    }

    @Override
    public MapType<String> getMap(final int index) {
        final JsonElement element = this.array.get(index);
        if (element instanceof JsonObject) {
            return new JsonMapType((JsonObject)element, this.compressed);
        }
        return null;
    }

    @Override
    public void setMap(final int index, final MapType<?> to) {
        this.array.set(index, ((JsonMapType)to).map);
    }

    @Override
    public String getString(final int index) {
        final JsonElement element = this.array.get(index);
        if (element instanceof JsonPrimitive) {
            final JsonPrimitive primitive = (JsonPrimitive)element;
            if (primitive.isString() || (this.compressed && primitive.isNumber())) {
                return primitive.getAsString();
            }
        }

        return null;
    }

    @Override
    public void setString(final int index, final String to) {
        this.array.set(index, new JsonPrimitive(to));
    }

    @Override
    public void addByte(final byte b) {
        this.array.add(Byte.valueOf(b));
    }

    @Override
    public void addByte(final int index, final byte b) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }

    @Override
    public void addShort(final short s) {
        this.array.add(Short.valueOf(s));
    }

    @Override
    public void addShort(final int index, final short s) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }

    @Override
    public void addInt(final int i) {
        this.array.add(Integer.valueOf(i));
    }

    @Override
    public void addInt(final int index, final int i) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }

    @Override
    public void addLong(final long l) {
        this.array.add(Long.valueOf(l));
    }

    @Override
    public void addLong(final int index, final long l) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }

    @Override
    public void addFloat(final float f) {
        this.array.add(Float.valueOf(f));
    }

    @Override
    public void addFloat(final int index, final float f) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }

    @Override
    public void addDouble(final double d) {
        this.array.add(Double.valueOf(d));
    }

    @Override
    public void addDouble(final int index, final double d) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }

    @Override
    public void addByteArray(final byte[] arr) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void addByteArray(final int index, final byte[] arr) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void addShortArray(final short[] arr) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void addShortArray(final int index, final short[] arr) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void addIntArray(final int[] arr) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void addIntArray(final int index, final int[] arr) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void addLongArray(final long[] arr) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void addLongArray(final int index, final long[] arr) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public void addList(final ListType list) {
        this.array.add(((JsonListType)list).array);
    }

    @Override
    public void addList(final int index, final ListType list) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }

    @Override
    public void addMap(final MapType<?> map) {
        this.array.add(((JsonMapType)map).map);
    }

    @Override
    public void addMap(final int index, final MapType<?> map) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }

    @Override
    public void addString(final String string) {
        this.array.add(string);
    }

    @Override
    public void addString(final int index, final String string) {
        // doesn't implement any methods for adding at index... yee haw...
        throw new UnsupportedOperationException();
    }
}
