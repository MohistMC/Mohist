package ca.spottedleaf.dataconverter.minecraft.walkers.generic;

import ca.spottedleaf.dataconverter.minecraft.converters.helpers.RenameHelper;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCDataType;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCValueType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;

public final class WalkerUtils {

    public static void convert(final MCDataType type, final MapType<String> data, final String path, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        final MapType<String> map = data.getMap(path);
        if (map != null) {
            final MapType<String> replace = type.convert(map, fromVersion, toVersion);
            if (replace != null) {
                data.setMap(path, replace);
            }
        }
    }

    public static void convertList(final MCDataType type, final MapType<String> data, final String path, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        final ListType list = data.getList(path, ObjectType.MAP);
        if (list != null) {
            for (int i = 0, len = list.size(); i < len; ++i) {
                final MapType<String> replace = type.convert(list.getMap(i), fromVersion, toVersion);
                if (replace != null) {
                    list.setMap(i, replace);
                }
            }
        }
    }

    public static void convert(final MCValueType type, final MapType<String> data, final String path, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        final Object value = data.getGeneric(path);
        if (value != null) {
            final Object converted = type.convert(value, fromVersion, toVersion);
            if (converted != null) {
                data.setGeneric(path, converted);
            }
        }
    }

    public static void convert(final MCValueType type, final ListType data, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        for (int i = 0, len = data.size(); i < len; ++i) {
            final Object value = data.getGeneric(i);
            final Object converted = type.convert(value, fromVersion, toVersion);
            if (converted != null) {
                data.setGeneric(i, converted);
            }
        }
    }

    public static void convertList(final MCValueType type, final MapType<String> data, final String path, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        final ListType list = data.getListUnchecked(path);
        if (list != null) {
            convert(type, list, fromVersion, toVersion);
        }
    }

    public static void convertKeys(final MCValueType type, final MapType<String> data, final String path, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        final MapType<String> map = data.getMap(path);
        if (map != null) {
            convertKeys(type, map, fromVersion, toVersion);
        }
    }

    public static void convertKeys(final MCValueType type, final MapType<String> data, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        RenameHelper.renameKeys(data, (final String input) -> {
            return (String)type.convert(input, fromVersion, toVersion);
        });
    }

    public static void convertValues(final MCDataType type, final MapType<String> data, final String path, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        convertValues(type, data.getMap(path), fromVersion, toVersion);
    }

    public static void convertValues(final MCDataType type, final MapType<String> data, final long fromVersion, final long toVersion) {
        if (data == null) {
            return;
        }

        for (final String key : data.keys()) {
            final MapType<String> value = data.getMap(key);
            if (value != null) {
                final MapType<String> replace = type.convert(value, fromVersion, toVersion);
                if (replace != null) {
                    // no CME, key is in map already
                    data.setMap(key, replace);
                }
            }
        }
    }
}
