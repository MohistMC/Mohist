package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V113 {

    protected static final int VERSION = MCVersions.V15W33C + 1;

    protected static void checkList(final MapType<String> data, final String id, final int requiredLength) {
        final ListType list = data.getList(id, ObjectType.FLOAT);
        if (list != null && list.size() == requiredLength) {
            for (int i = 0; i < requiredLength; ++i) {
                if (list.getFloat(i) != 0.0F) {
                    return;
                }
            }
        }

        data.remove(id);
    }

    public static void register() {
        // Removes "HandDropChances" and "ArmorDropChances" if they're empty.
        MCTypeRegistry.ENTITY.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                checkList(data, "HandDropChances", 2);
                checkList(data, "ArmorDropChances", 4);
                return null;
            }
        });
    }

    private V113() {}

}
