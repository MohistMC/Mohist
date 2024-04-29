package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItems;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V2511 {

    protected static final int VERSION = MCVersions.V20W09A + 1;

    private V2511() {}

    private static int[] createUUIDArray(final long most, final long least) {
        return new int[] {
                (int)(most >>> 32),
                (int)most,
                (int)(least >>> 32),
                (int)least
        };
    }

    private static void setUUID(final MapType<String> data, final long most, final long least) {
        if (most != 0L && least != 0L) {
            data.setInts("OwnerUUID", createUUIDArray(most, least));
        }
    }

    public static void register() {
        final DataConverter<MapType<String>, MapType<String>> throwableConverter = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> owner = data.getMap("owner");
                data.remove("owner");
                if (owner == null) {
                    return null;
                }

                setUUID(data, owner.getLong("M"), owner.getLong("L"));

                return null;
            }
        };
        final DataConverter<MapType<String>, MapType<String>> potionConverter = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> potion = data.getMap("Potion");
                data.remove("Potion");

                data.setMap("Item", potion == null ? Types.NBT.createEmptyMap() : potion);

                return null;
            }
        };
        final DataConverter<MapType<String>, MapType<String>> llamaSpitConverter = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> owner = data.getMap("Owner");
                data.remove("Owner");
                if (owner == null) {
                    return null;
                }

                setUUID(data, owner.getLong("OwnerUUIDMost"), owner.getLong("OwnerUUIDLeast"));

                return null;
            }
        };
        final DataConverter<MapType<String>, MapType<String>> arrowConverter = new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                setUUID(data, data.getLong("OwnerUUIDMost"), data.getLong("OwnerUUIDLeast"));

                data.remove("OwnerUUIDMost");
                data.remove("OwnerUUIDLeast");

                return null;
            }
        };

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:egg", throwableConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:ender_pearl", throwableConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:experience_bottle", throwableConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:snowball", throwableConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:potion", throwableConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:potion", potionConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:llama_spit", llamaSpitConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:arrow", arrowConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:spectral_arrow", arrowConverter);
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:trident", arrowConverter);

        // Vanilla migrates the potion item but does not change the schema.
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:potion", new DataWalkerItems("Item"));
    }
}
