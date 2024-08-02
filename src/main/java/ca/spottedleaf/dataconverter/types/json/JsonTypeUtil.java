package ca.spottedleaf.dataconverter.types.json;

import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.TypeUtil;
import ca.spottedleaf.dataconverter.types.nbt.NBTListType;
import ca.spottedleaf.dataconverter.types.nbt.NBTMapType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.util.Map;

public final class JsonTypeUtil implements TypeUtil {

    @Override
    public ListType createEmptyList() {
        return new JsonListType(false);
    }

    @Override
    public MapType<String> createEmptyMap() {
        return new JsonMapType(false);
    }

    public static <T extends JsonElement> T copyJson(final T from) {
        // This is stupidly inefficient. However, deepCopy() is not exposed in this gson version.
        final String out = from.toString();

        return (T)Streams.parse(new JsonReader(new StringReader(out)));
    }

    private static Object convertToGenericNBT(final JsonElement element, final boolean compressed) {
        if (element instanceof JsonObject) {
            return convertJsonToNBT(new JsonMapType((JsonObject)element, compressed));
        } else if (element instanceof JsonArray) {
            return convertJsonToNBT(new JsonListType((JsonArray)element, compressed));
        } else if (element instanceof JsonNull) {
            return null;
        } else {
            final JsonPrimitive primitive = (JsonPrimitive)element;
            if (primitive.isBoolean()) {
                return primitive.getAsBoolean() ? Byte.valueOf((byte)1) : Byte.valueOf((byte)0);
            } else if (primitive.isNumber()) {
                return primitive.getAsNumber();
            } else if (primitive.isString()) {
                return primitive.getAsString();
            }
        }

        throw new IllegalStateException("Unrecognized type " + element);
    }

    public static NBTMapType convertJsonToNBT(final JsonMapType json) {
        final NBTMapType ret = new NBTMapType();
        for (final Map.Entry<String, JsonElement> entry : json.map.entrySet()) {
            final Object obj = convertToGenericNBT(entry.getValue(), json.compressed);
            if (obj == null) {
                continue;
            }

            ret.setGeneric(entry.getKey(), obj);
        }

        return ret;
    }

    public static NBTListType convertJsonToNBT(final JsonListType json) {
        final NBTListType ret = new NBTListType();

        for (int i = 0, len = json.size(); i < len; ++i) {
            ret.addGeneric(convertToGenericNBT(json.array.get(i), json.compressed));
        }

        return ret;
    }
}
