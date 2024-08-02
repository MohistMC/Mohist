package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V1904 {

    protected static final int VERSION = MCVersions.V18W43C + 1;

    private V1904() {}

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:ocelot", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final int catType = data.getInt("CatType");

                if (catType == 0) {
                    final String owner = data.getString("Owner");
                    final String ownerUUID = data.getString("OwnerUUID");
                    if ((owner != null && owner.length() > 0) || (ownerUUID != null && ownerUUID.length() > 0)) {
                        data.setBoolean("Trusting", true);
                    }
                } else if (catType > 0 && catType < 4) {
                    data.setString("id", "minecraft:cat");
                    data.setString("OwnerUUID", data.getString("OwnerUUID", ""));
                }

                return null;
            }
        });

        registerMob("minecraft:cat");
    }


}
