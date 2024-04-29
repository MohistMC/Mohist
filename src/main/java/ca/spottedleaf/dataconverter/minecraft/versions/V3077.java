package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;

public final class V3077 {

    protected static final int VERSION = MCVersions.V1_18_2 + 102;

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final boolean isLightOn = data.getBoolean("isLightOn");
                if (isLightOn) {
                    return null;
                }

                final ListType sections = data.getList("sections", ObjectType.MAP);
                if (sections == null) {
                    return null;
                }

                for (int i = 0, len = sections.size(); i < len; ++i) {
                    final MapType<String> section = sections.getMap(i);
                    section.remove("BlockLight");
                    section.remove("SkyLight");
                }

                return null;
            }
        });
    }
}
