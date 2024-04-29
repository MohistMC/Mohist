package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.MapType;

public final class V808 {

    protected static final int VERSION = MCVersions.V16W38A + 1;

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:shulker", new DataConverter<>(VERSION, 1) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (!data.hasKey("Color", ObjectType.NUMBER)) {
                    data.setByte("Color", (byte)10);
                }
                return null;
            }
        });

        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:shulker_box", new DataWalkerItemLists("Items"));
    }

    private V808() {}

}
