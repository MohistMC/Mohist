package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V1925 {

    protected static final int VERSION = MCVersions.V19W03C + 1;

    public static void register() {
        MCTypeRegistry.SAVED_DATA.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> root, final long sourceVersion, final long toVersion) {
                final MapType<String> data = root.getMap("data");
                if (data == null) {
                    final MapType<String> ret = Types.NBT.createEmptyMap();
                    ret.setMap("data", root);

                    return ret;
                }
                return null;
            }
        });
    }

}
