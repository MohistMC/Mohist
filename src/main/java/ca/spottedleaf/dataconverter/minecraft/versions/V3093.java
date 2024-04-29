package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V3093 {

    protected static final int VERSION = MCVersions.V22W17A;

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:goat", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                data.setBoolean("HasLeftHorn", true);
                data.setBoolean("HasRightHorn", true);
                return null;
            }
        });
    }
}
