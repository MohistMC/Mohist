package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;

public final class V2535 {

    protected static final int VERSION = MCVersions.V20W19A + 1;

    private V2535() {}

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:shulker", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                // Mojang uses doubles for whatever reason... rotation is in FLOAT. by using double here
                // the entity load will just ignore rotation and set it to 0...
                final ListType rotation = data.getList("Rotation", ObjectType.FLOAT);

                if (rotation == null || rotation.size() == 0) {
                    return null;
                }

                rotation.setFloat(0, rotation.getFloat(0) - 180.0F);

                return null;
            }
        });
    }
}
