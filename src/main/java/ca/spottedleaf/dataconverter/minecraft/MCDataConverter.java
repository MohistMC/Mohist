package ca.spottedleaf.dataconverter.minecraft;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.converters.datatypes.DataType;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCDataType;
import ca.spottedleaf.dataconverter.types.json.JsonMapType;
import ca.spottedleaf.dataconverter.types.nbt.NBTMapType;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.nbt.CompoundTag;

public final class MCDataConverter {

    private static final LongArrayList BREAKPOINTS = MCVersionRegistry.getBreakpoints();

    public static <T> T copy(final T type) {
        if (type instanceof CompoundTag) {
            return (T)((CompoundTag)type).copy();
        } else if (type instanceof JsonObject) {
            return (T)((JsonObject)type).deepCopy();
        }

        return type;
    }

    public static CompoundTag convertTag(final MCDataType type, final CompoundTag data, final int fromVersion, final int toVersion) {
        final NBTMapType wrapped = new NBTMapType(data);

        final NBTMapType replaced = (NBTMapType)convert(type, wrapped, fromVersion, toVersion);

        return replaced == null ? wrapped.getTag() : replaced.getTag();
    }

    public static JsonObject convertJson(final MCDataType type, final JsonObject data, final boolean compressed, final int fromVersion, final int toVersion) {
        final JsonMapType wrapped = new JsonMapType(data, compressed);

        final JsonMapType replaced = (JsonMapType)convert(type, wrapped, fromVersion, toVersion);

        return replaced == null ? wrapped.getJson() : replaced.getJson();
    }

    public static <T, R> R convert(final DataType<T, R> type, final T data, int fromVersion, final int toVersion) {
        Object ret = data;

        long currentVersion = DataConverter.encodeVersions(fromVersion < 99 ? 99 : fromVersion, Integer.MAX_VALUE);
        final long nextVersion = DataConverter.encodeVersions(toVersion, Integer.MAX_VALUE);

        for (int i = 0, len = BREAKPOINTS.size(); i < len; ++i) {
            final long breakpoint = BREAKPOINTS.getLong(i);

            if (currentVersion >= breakpoint) {
                continue;
            }

            final Object converted = type.convert((T)ret, currentVersion, Math.min(nextVersion, breakpoint - 1));
            if (converted != null) {
                ret = converted;
            }

            currentVersion = Math.min(nextVersion, breakpoint - 1);

            if (currentVersion == nextVersion) {
                break;
            }
        }

        if (currentVersion != nextVersion) {
            final Object converted = type.convert((T)ret, currentVersion, nextVersion);
            if (converted != null) {
                ret = converted;
            }
        }

        return (R)ret;
    }

    private MCDataConverter() {}

}
