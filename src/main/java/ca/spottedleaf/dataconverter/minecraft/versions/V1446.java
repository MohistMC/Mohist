package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V1446 {

    protected static final int VERSION = MCVersions.V17W43B + 1;

    public static void register() {
        MCTypeRegistry.OPTIONS.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                for (final String key : data.keys()) {
                    if (!key.startsWith("key_")) {
                        continue;
                    }

                    final String value = data.getString(key);

                    if (value.startsWith("key.mouse") || value.startsWith("scancode.")) {
                        continue;
                    }

                    data.setString(key, "key.keyboard." + value.substring("key.".length()));
                }
                return null;
            }
        });
    }

    private V1446() {}

}
