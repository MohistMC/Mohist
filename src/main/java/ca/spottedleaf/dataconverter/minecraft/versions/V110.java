package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V110 {

    protected static final int VERSION = MCVersions.V15W32C + 6;

    public static void register() {
        // Moves the Saddle boolean to be an actual saddle item. Note: The data walker for the SaddleItem exists
        // in V99, it doesn't need to be added here.
        MCTypeRegistry.ENTITY.addConverterForId("EntityHorse", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (!data.getBoolean("Saddle") || data.hasKey("SaddleItem", ObjectType.MAP)) {
                    return null;
                }

                final MapType<String> saddleItem = Types.NBT.createEmptyMap();
                data.remove("Saddle");
                data.setMap("SaddleItem", saddleItem);

                saddleItem.setString("id", "minecraft:saddle");
                saddleItem.setByte("Count", (byte)1);
                saddleItem.setShort("Damage", (short)0);

                return null;
            }
        });
    }

    private V110() {}

}
