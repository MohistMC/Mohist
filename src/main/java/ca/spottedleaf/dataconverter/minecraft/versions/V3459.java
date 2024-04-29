package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V3459 {

    private static final int VERSION = MCVersions.V1_20_PRE5 + 1;

    public static void register() {
        MCTypeRegistry.LEVEL.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (data.hasKey("DragonFight")) {
                    return null;
                }

                final MapType<String> dimensionData = data.getMap("DimensionData");
                if (dimensionData == null) {
                    return null;
                }

                final MapType<String> endData = dimensionData.getMap("1");
                if (endData != null) {
                    data.setMap("DragonFight", endData.getMap("DragonFight", endData.getTypeUtil().createEmptyMap()).copy());
                }

                return null;
            }
        });
    }
}
