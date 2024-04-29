package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V1953 {

    protected static final int VERSION = MCVersions.V1_14 + 1;

    private V1953() {}

    public static void register() {
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:banner", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String name = data.getString("CustomName");
                if (name != null) {
                    data.setString("CustomName", name.replace("\"translate\":\"block.minecraft.illager_banner\"", "\"translate\":\"block.minecraft.ominous_banner\""));
                }
                return null;
            }
        });
    }

}
