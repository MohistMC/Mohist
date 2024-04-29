package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V107 {

    protected static final int VERSION = MCVersions.V15W32C + 3;

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("Minecart", new DataConverter<>(VERSION) {
            protected final String[] MINECART_IDS = new String[] {
                    "MinecartRideable",     // 0
                    "MinecartChest",        // 1
                    "MinecartFurnace",      // 2
                    "MinecartTNT",          // 3
                    "MinecartSpawner",      // 4
                    "MinecartHopper",       // 5
                    "MinecartCommandBlock"  // 6
            };
            // Vanilla does not use all of the IDs here. The legacy (pre DFU) code does, so I'm going to use them.
            // No harm in catching more cases here.

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                String newId = "MinecartRideable"; // dfl
                final int type = data.getInt("Type");
                data.remove("Type");

                if (type >= 0 && type < MINECART_IDS.length) {
                    newId = MINECART_IDS[type];
                }
                data.setString("id", newId);

                return null;
            }
        });
    }

    private V107() {}

}
