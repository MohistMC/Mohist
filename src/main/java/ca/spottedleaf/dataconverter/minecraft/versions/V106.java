package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V106 {

    protected static final int VERSION = MCVersions.V15W32C + 2;

    public static void register() {
        // V106 -> V15W32C + 2

        MCTypeRegistry.UNTAGGED_SPAWNER.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                // While all converters for spawners check the id for this version, we don't because spawners exist in minecarts. ooops! Loading a chunk
                // with a minecart spawner from 1.7.10 in 1.16.5 vanilla will fail to convert! Clearly there was a mistake in how they
                // used and applied spawner converters. In anycase, do not check the id - we are not guaranteed to be a tile
                // entity. We can be a regular old minecart spawner. And we know we are a spawner because this is only called from data walkers.

                final String entityId = data.getString("EntityId");
                if (entityId != null) {
                    data.remove("EntityId");
                    MapType<String> spawnData = data.getMap("SpawnData");
                    if (spawnData == null) {
                        spawnData = Types.NBT.createEmptyMap();
                        data.setMap("SpawnData", spawnData);
                    }
                    spawnData.setString("id", entityId.isEmpty() ? "Pig" : entityId);
                }

                final ListType spawnPotentials = data.getList("SpawnPotentials", ObjectType.MAP);
                if (spawnPotentials != null) {
                    for (int i = 0, len = spawnPotentials.size(); i < len; ++i) {
                        // convert to standard entity format (it's not a coincidence a walker for spawners is only added
                        // in this version)
                        final MapType<String> spawn = spawnPotentials.getMap(i);
                        final String spawnType = spawn.getString("Type");
                        if (spawnType == null) {
                            continue;
                        }
                        spawn.remove("Type");

                        MapType<String> properties = spawn.getMap("Properties");
                        if (properties == null) {
                            properties = Types.NBT.createEmptyMap();
                        } else {
                            spawn.remove("Properties");
                        }

                        properties.setString("id", spawnType);

                        spawn.setMap("Entity", properties);
                    }
                }

                return null;
            }
        });

        MCTypeRegistry.UNTAGGED_SPAWNER.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final ListType spawnPotentials = data.getList("SpawnPotentials", ObjectType.MAP);
            if (spawnPotentials != null) {
                for (int i = 0, len = spawnPotentials.size(); i < len; ++i) {
                    final MapType<String> spawnPotential = spawnPotentials.getMap(i);
                    WalkerUtils.convert(MCTypeRegistry.ENTITY, spawnPotential, "Entity", fromVersion, toVersion);
                }
            }

            WalkerUtils.convert(MCTypeRegistry.ENTITY, data, "SpawnData", fromVersion, toVersion);

            return null;
        });
    }

    private V106() {}

}
