package ca.spottedleaf.dataconverter.util;

import ca.spottedleaf.dataconverter.types.MapType;
import net.minecraft.resources.ResourceLocation;

public final class NamespaceUtil {

    private NamespaceUtil() {}

    public static void enforceForPath(final MapType<String> data, final String path) {
        if (data == null) {
            return;
        }

        final String id = data.getString(path);
        if (id != null) {
            final String replace = NamespaceUtil.correctNamespaceOrNull(id);
            if (replace != null) {
                data.setString(path, replace);
            }
        }
    }

    public static String correctNamespace(final String value) {
        if (value == null) {
            return null;
        }
        final ResourceLocation resourceLocation = ResourceLocation.tryParse(value);
        return resourceLocation != null ? resourceLocation.toString() : value;
    }

    public static String correctNamespaceOrNull(final String value) {
        if (value == null) {
            return null;
        }
        final String correct = correctNamespace(value);
        return correct.equals(value) ? null : correct;
    }
}
