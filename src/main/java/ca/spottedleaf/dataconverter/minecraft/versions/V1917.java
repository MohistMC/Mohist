package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V1917 {

    protected static final int VERSION = MCVersions.V18W49A + 1;

    private V1917() {}

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:cat", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (data.getInt("CatType") == 9) {
                    data.setInt("CatType", 10);
                }
                return null;
            }
        });
    }

}
