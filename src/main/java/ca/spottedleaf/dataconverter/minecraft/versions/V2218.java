package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V2218 {

    protected static final int VERSION = MCVersions.V1_15_PRE1;

    private V2218() {}

    public static void register() {
        MCTypeRegistry.POI_CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> sections = data.getMap("Sections");
                if (sections == null) {
                    return null;
                }

                for (final String key : sections.keys()) {
                    final MapType<String> section = sections.getMap(key);

                    section.remove("Valid");
                }

                return null;
            }
        });
    }
}
