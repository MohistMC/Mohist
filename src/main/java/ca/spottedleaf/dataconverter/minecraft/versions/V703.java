package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItems;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V703 {

    protected static final int VERSION = MCVersions.V1_10_2 + 191;

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("EntityHorse", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final int type = data.getInt("Type");
                data.remove("Type");

                switch (type) {
                    case 0:
                    default:
                        data.setString("id", "Horse");
                        break;

                    case 1:
                        data.setString("id", "Donkey");
                        break;

                    case 2:
                        data.setString("id", "Mule");
                        break;

                    case 3:
                        data.setString("id", "ZombieHorse");
                        break;

                    case 4:
                        data.setString("id", "SkeletonHorse");
                        break;
                }

                return null;
            }
        });

        MCTypeRegistry.ENTITY.addWalker(VERSION, "Horse", new DataWalkerItems("ArmorItem", "SaddleItem"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Horse", new DataWalkerItemLists("ArmorItems", "HandItems"));

        MCTypeRegistry.ENTITY.addWalker(VERSION, "Donkey", new DataWalkerItems("SaddleItem"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Donkey", new DataWalkerItemLists("Items", "ArmorItems", "HandItems"));

        MCTypeRegistry.ENTITY.addWalker(VERSION, "Mule", new DataWalkerItems("SaddleItem"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "Mule", new DataWalkerItemLists("Items", "ArmorItems", "HandItems"));

        MCTypeRegistry.ENTITY.addWalker(VERSION, "ZombieHorse", new DataWalkerItems("SaddleItem"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "ZombieHorse", new DataWalkerItemLists("ArmorItems", "HandItems"));

        MCTypeRegistry.ENTITY.addWalker(VERSION, "SkeletonHorse", new DataWalkerItems("SaddleItem"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "SkeletonHorse", new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    private V703() {}

}
