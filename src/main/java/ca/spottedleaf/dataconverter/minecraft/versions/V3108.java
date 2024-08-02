package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V3108 {

    private static final int VERSION = MCVersions.V1_19_1_PRE1 + 1;

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> context = data.getMap("__context");
                if ("minecraft:overworld".equals(context == null ? null : context.getString("dimension"))) {
                    return null;
                }

                data.remove("blending_data");

                return null;
            }
        });
    }
}
