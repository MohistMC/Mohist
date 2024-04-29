package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V3201 {

    private static final int VERSION = MCVersions.V1_19_2 + 81;

    public static void register() {
        MCTypeRegistry.OPTIONS.addStructureConverter(new DataConverter<>(VERSION) {
            private static void fixList(final MapType<String> data, final String target) {
                if (data == null) {
                    return;
                }
                final String curr = data.getString(target);
                if (curr == null) {
                    return;
                }
                data.setString(target, curr.replace("\"programer_art\"", "\"programmer_art\""));
            }

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                fixList(data, "resourcePacks");
                fixList(data, "incompatibleResourcePacks");
                return null;
            }
        });
    }
}
