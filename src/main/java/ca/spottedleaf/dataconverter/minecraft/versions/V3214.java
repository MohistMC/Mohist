package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V3214 {

    private static final int VERSION = MCVersions.V1_19_3_PRE3 + 1;

    public static void register() {
        MCTypeRegistry.OPTIONS.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String value = data.getString("ao");

                if ("0".equals(value)) {
                    data.setString("ao", "false");
                } else if ("1".equals(value) || "2".equals(value)) {
                    data.setString("ao", "true");
                }

                return null;
            }
        });
    }
}
