package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V2702 {

    protected static final int VERSION = MCVersions.V21W10A + 3;

    public static void register() {
        final DataConverter<MapType<String>, MapType<String>> arrowConverter = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (data.hasKey("pickup")) {
                    return null;
                }

                final boolean player = data.getBoolean("player", true);
                data.remove("player");

                data.setByte("pickup", player ? (byte)1 : (byte)0);

                return null;
            }
        };

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:arrow", arrowConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:spectral_arrow", arrowConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:trident", arrowConverter);
    }
}
