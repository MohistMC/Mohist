package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V2505 {

    protected static final int VERSION = MCVersions.V20W06A + 1;

    private V2505() {}

    private static void registerMob(final String id) {
        MCTypeRegistry.ENTITY.addWalker(VERSION, id, new DataWalkerItemLists("ArmorItems", "HandItems"));
    }

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:villager", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> brain = data.getMap("Brain");
                if (brain == null) {
                    return null;
                }

                final MapType<String> memories = brain.getMap("memories");
                if (memories == null) {
                    return null;
                }

                for (final String key : memories.keys()) {
                    final Object value = memories.getGeneric(key);

                    final MapType<String> wrapped = Types.NBT.createEmptyMap();
                    wrapped.setGeneric("value", value);

                    memories.setMap(key, wrapped);
                }

                return null;
            }
        });

        registerMob("minecraft:piglin");
    }
}
