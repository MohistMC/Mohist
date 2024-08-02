package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;

public final class V1963 {

    protected static final int VERSION = MCVersions.V1_14_2;

    private V1963() {}

    public static void register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:villager", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final ListType gossips = data.getList("Gossips", ObjectType.MAP);
                if (gossips == null) {
                    return null;
                }

                for (int i = 0; i < gossips.size();) {
                    final MapType<String> gossip = gossips.getMap(i);
                    if ("golem".equals(gossip.getString("Type"))) {
                        gossips.remove(i);
                        continue;
                    }

                    ++i;
                }

                return null;
            }
        });
    }

}
