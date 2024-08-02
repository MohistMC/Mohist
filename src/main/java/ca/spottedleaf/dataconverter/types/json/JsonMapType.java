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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class JsonMapType implements MapType<String> {

    protected final JsonObject map;
    protected final boolean compressed;

    public JsonMapType(final boolean compressed) {
        this.map = new JsonObject();
        this.compressed = compressed;
    }

    public JsonMapType(final JsonObject map, final boolean compressed) {
        this.map = map;
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

        if (obj == null || obj.getClass() != JsonMapType.class) {
            return false;
        }

        return this.map.equals(((JsonMapType)obj).map);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public String toString() {
        return "JsonMapType{" +
                "map=" + this.map +
                ", compressed=" + this.compressed +
                '}';
    }

    public JsonObject getJson() {
        return this.map;
    }

    @Override
    public int size() {
        return this.map.entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.entrySet().isEmpty();
    }

    @Override
    public void clear() {
        this.map.entrySet().clear();
    }

    @Override
    public Set<String> keys() {
        // ah shit. no keyset method
        final Set<String> keys = new LinkedHashSet<>();

        for (final Map.Entry<String, JsonElement> entry : this.map.entrySet()) {
            keys.add(entry.getKey());
        }

        return keys;
    }

    @Override
    public MapType<String> copy() {
        return new JsonMapType(JsonTypeUtil.copyJson(this.map), this.compressed);
    }

    @Override
    public boolean hasKey(final String key) {
        return this.map.has(key);
    }

    @Override
    public boolean hasKey(final String key, final ObjectType type) {
        final JsonElement element = this.map.get(key);
        if (element == null) {
            return false;
        }

        if (type == ObjectType.UNDEFINED) {
            return true;
        }

        if (element.isJsonArray()) {
            return type == ObjectType.LIST;
        } else if (element.isJsonObject()) {
            return type == ObjectType.MAP;
        } else if (element.isJsonNull()) {
            return false;
        }

        final JsonPrimitive primitive = (JsonPrimitive)element;
        if (primitive.isString()) {
            return type == ObjectType.STRING || (this.compressed && type == ObjectType.NUMBER);
        } else if (primitive.isBoolean()) {
            return type.isNumber();
        } else {
            // is number
            final Number number = primitive.getAsNumber();
            if (number instanceof Byte) {
                return type == ObjectType.BYTE || (this.compressed && type == ObjectType.STRING);
            } else if (number instanceof Short) {
                return type == ObjectType.SHORT || (this.compressed && type == ObjectType.STRING);
            } else if (number instanceof Integer) {
                return type == ObjectType.INT || (this.compressed && type == ObjectType.STRING);
            } else if (number instanceof Long) {
                return type == ObjectType.LONG || (this.compressed && type == ObjectType.STRING);
            } else if (number instanceof Float) {
                return type == ObjectType.FLOAT || (this.compressed && type == ObjectType.STRING);
            } else {
                return type == ObjectType.DOUBLE || (this.compressed && type == ObjectType.STRING);
            }
        }
    }

    @Override
    public void remove(final String key) {
        this.map.remove(key);
    }

    @Override
    public Object getGeneric(final String key) {
        final JsonElement element = this.map.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        } else if (element.isJsonObject()) {
            return new JsonMapType((JsonObject)element, this.compressed);
        } else if (element.isJsonArray()) {
            return new JsonListType((JsonArray)element, this.compressed);
        } else {
            // primitive
            final JsonPrimitive primitive = (JsonPrimitive)element;
            if (primitive.isNumber()) {
                return primitive.getAsNumber();
            } else if (primitive.isString()) {
                return primitive.getAsString();
            } else if (primitive.isBoolean()) {
                return Boolean.valueOf(primitive.getAsBoolean());
            } else {
                throw new IllegalStateException("Unknown json object " + element);
            }
        }
    }

    @Override
    public Number getNumber(final String key) {
        return this.getNumber(key, null);
    }

    @Override
    public Number getNumber(final String key, final Number dfl) {
        final JsonElement element = this.map.get(key);
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

        return dfl;
    }

    @Override
    public boolean getBoolean(final String key) {
        return this.getBoolean(key, false);
    }

    @Override
    public boolean getBoolean(final String key, final boolean dfl) {
        final JsonElement element = this.map.get(key);
        if (element instanceof JsonPrimitive) {
            final JsonPrimitive primitive = (JsonPrimitive)element;
            if (primitive.isNumber()) {
                return primitive.getAsNumber().byteValue() != 0;
            } else if (primitive.isBoolean()) {
                return primitive.getAsBoolean();
            }
        }

        return dfl;
    }

    @Override
    public void setBoolean(final String key, final boolean val) {
        this.map.addProperty(key, Boolean.valueOf(val));
    }

    @Override
    public byte getByte(final String key) {
        return this.getByte(key, (byte)0);
    }

    @Override
    public byte getByte(final String key, final byte dfl) {
        final Number ret = this.getNumber(key, null);
        return ret == null ? dfl : ret.byteValue();
    }

    @Override
    public void setByte(final String key, final byte val) {
        this.map.addProperty(key, Byte.valueOf(val));
    }

    @Override
    public short getShort(final String key) {
        return this.getShort(key, (short)0);
    }

    @Override
    public short getShort(final String key, final short dfl) {
        final Number ret = this.getNumber(key, null);
        return ret == null ? dfl : ret.shortValue();
    }

    @Override
    public void setShort(final String key, final short val) {
        this.map.addProperty(key, Short.valueOf(val));
    }

    @Override
    public int getInt(final String key) {
        return this.getInt(key, 0);
    }

    @Override
    public int getInt(final String key, final int dfl) {
        final Number ret = this.getNumber(key, null);
        return ret == null ? dfl : ret.intValue();
    }

    @Override
    public void setInt(final String key, final int val) {
        this.map.addProperty(key, Integer.valueOf(val));
    }

    @Override
    public long getLong(final String key) {
        return this.getLong(key, 0L);
    }

    @Override
    public long getLong(final String key, final long dfl) {
        final Number ret = this.getNumber(key, null);
        return ret == null ? dfl : ret.longValue();
    }

    @Override
    public void setLong(final String key, final long val) {
        this.map.addProperty(key, Long.valueOf(val));
    }

    @Override
    public float getFloat(final String key) {
        return this.getFloat(key, 0.0F);
    }

    @Override
    public float getFloat(final String key, final float dfl) {
        final Number ret = this.getNumber(key, null);
        return ret == null ? dfl : ret.floatValue();
    }

    @Override
    public void setFloat(final String key, final float val) {
        this.map.addProperty(key, Float.valueOf(val));
    }

    @Override
    public double getDouble(final String key) {
        return this.getDouble(key, 0.0D);
    }

    @Override
    public double getDouble(final String key, final double dfl) {
        final Number ret = this.getNumber(key, null);
        return ret == null ? dfl : ret.doubleValue();
    }

    @Override
    public void setDouble(final String key, final double val) {
        this.map.addProperty(key, Double.valueOf(val));
    }

    @Override
    public byte[] getBytes(final String key) {
        return this.getBytes(key, null);
    }

    @Override
    public byte[] getBytes(final String key, final byte[] dfl) {
        return dfl;
    }

    @Override
    public void setBytes(final String key, final byte[] val) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public short[] getShorts(final String key) {
        return this.getShorts(key, null);
    }

    @Override
    public short[] getShorts(final String key, final short[] dfl) {
        return dfl;
    }

    @Override
    public void setShorts(final String key, final short[] val) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] getInts(final String key) {
        return this.getInts(key, null);
    }

    @Override
    public int[] getInts(final String key, final int[] dfl) {
        return dfl;
    }

    @Override
    public void setInts(final String key, final int[] val) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getLongs(final String key) {
        return this.getLongs(key, null);
    }

    @Override
    public long[] getLongs(final String key, final long[] dfl) {
        return dfl;
    }

    @Override
    public void setLongs(final String key, final long[] val) {
        // JSON does not support raw primitive arrays
        throw new UnsupportedOperationException();
    }

    @Override
    public ListType getListUnchecked(final String key) {
        return this.getListUnchecked(key, null);
    }

    @Override
    public ListType getListUnchecked(final String key, final ListType dfl) {
        final JsonElement element = this.map.get(key);
        if (element instanceof JsonArray) {
            return new JsonListType((JsonArray)element, this.compressed);
        }

        return dfl;
    }

    @Override
    public void setList(final String key, final ListType val) {
        this.map.add(key, ((JsonListType)val).getJson());
    }

    @Override
    public MapType getMap(final String key) {
        return this.getMap(key, null);
    }

    @Override
    public MapType getMap(final String key, final MapType dfl) {
        final JsonElement element = this.map.get(key);
        if (element instanceof JsonObject) {
            return new JsonMapType((JsonObject)element, this.compressed);
        }

        return dfl;
    }

    @Override
    public void setMap(final String key, final MapType<?> val) {
        this.map.add(key, ((JsonMapType)val).map);
    }

    @Override
    public String getString(final String key) {
        return this.getString(key, null);
    }

    @Override
    public String getString(final String key, final String dfl) {
        final JsonElement element = this.map.get(key);
        if (element instanceof JsonPrimitive) {
            final JsonPrimitive primitive = (JsonPrimitive)element;
            if (primitive.isString()) {
                return primitive.getAsString();
            } else if (this.compressed && primitive.isNumber()) {
                return primitive.getAsString();
            }
        }

        return dfl;
    }

    @Override
    public void setString(final String key, final String val) {
        this.map.addProperty(key, val);
    }
}
