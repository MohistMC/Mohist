package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V2831 {

    protected static final int VERSION = MCVersions.V1_17_1 + 101;

    public static void register() {
        MCTypeRegistry.UNTAGGED_SPAWNER.addStructureWalker(VERSION, (final MapType<String> root, final long fromVersion, final long toVersion) -> {
            final ListType spawnPotentials = root.getList("SpawnPotentials", ObjectType.MAP);
            if (spawnPotentials != null) {
                for (int i = 0, len = spawnPotentials.size(); i < len; ++i) {
                    final MapType<String> spawnPotential = spawnPotentials.getMap(i);

                    WalkerUtils.convert(MCTypeRegistry.ENTITY, spawnPotential.getMap("data"), "entity", fromVersion, toVersion);
                }
            }

            WalkerUtils.convert(MCTypeRegistry.ENTITY, root.getMap("SpawnData"), "entity", fromVersion, toVersion);

            return null;
        });

        MCTypeRegistry.UNTAGGED_SPAWNER.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> root, final long sourceVersion, final long toVersion) {
                final MapType<String> spawnData = root.getMap("SpawnData");
                if (spawnData != null) {
                    final MapType<String> wrapped = Types.NBT.createEmptyMap();
                    root.setMap("SpawnData", wrapped);

                    wrapped.setMap("entity", spawnData);
                }

                final ListType spawnPotentials = root.getList("SpawnPotentials", ObjectType.MAP);
                if (spawnPotentials != null) {
                    for (int i = 0, len = spawnPotentials.size(); i < len; ++i) {
                        final MapType<String> spawnPotential = spawnPotentials.getMap(i);

                        // new format of weighted list (SpawnPotentials):
                        // root.data -> data
                        // root.weight -> weight

                        final MapType<String> entity = spawnPotential.getMap("Entity");
                        final int weight = spawnPotential.getInt("Weight", 1);
                        spawnPotential.remove("Entity");
                        spawnPotential.remove("Weight");
                        spawnPotential.setInt("weight", weight);

                        final MapType<String> data = Types.NBT.createEmptyMap();
                        spawnPotential.setMap("data", data);

                        data.setMap("entity", entity);
                    }
                }

                return null;
            }
        });
    }
}
