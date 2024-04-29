package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V701 {

    protected static final int VERSION = MCVersions.V1_10_2 + 189;

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("Skeleton", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final int type = data.getInt("SkeletonType");
                data.remove("SkeletonType");

                switch (type) {
                    case 1:
                        data.setString("id", "WitherSkeleton");
                        break;
                    case 2:
                        data.setString("id", "Stray");
                        break;
                }

                return null;
            }
        });

        registerMob("WitherSkeleton");
        registerMob("Stray");
    }

    private V701() {}

}
