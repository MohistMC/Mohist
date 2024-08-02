package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V1914 {

    protected static final int VERSION = MCVersions.V18W48A;

    public static void register() {
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:chest", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String lootTable = data.getString("LootTable");

                if ("minecraft:chests/village_blacksmith".equals(lootTable)) {
                    data.setString("LootTable", "minecraft:chests/village/village_weaponsmith");
                }

                return null;
            }
        });
    }

}
