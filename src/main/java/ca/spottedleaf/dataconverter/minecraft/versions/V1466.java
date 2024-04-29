package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V1466 {

    protected static final int VERSION = MCVersions.V18W06A;

    protected static short packOffsetCoordinates(final int x, final int y, final int z) {
        return (short)((x & 15) | ((y & 15) << 4) | ((z & 15) << 8));
    }

    public static void register() {
        // There is a rather critical change I've made to this converter: changing the chunk status determination.
        // In Vanilla, this is determined by whether the terrain has been populated and whether the chunk is lit.
        // For reference, here is the full status progression (at the time of 18w06a):
        // empty -> base -> carved -> decorated -> lighted -> mobs_spawned -> finalized -> fullchunk -> postprocessed
        // So one of those must be picked.
        // If the chunk is lit and terrain is populated, the Vanilla converter will set the status to "mobs_spawned."
        // If it is anything else, it will be "empty"
        // I've changed it to the following: if terrain is populated, it is set to at least decorated. If it is populated
        // and lit, it is set to "mobs_spawned"
        // But what if it is not populated? If it is not populated, ignore the lit field - obviously that's just broken.
        // It can't be lit and not populated.
        // Let's take a look at chunk generation logic for a chunk that is not populated, or even near a populated chunk.
        // It actually will generate a chunk up to the "carved" stage. It generates the base terrain, (i.e using noise
        // to figure out where stone is, dirt, grass) and it will generate caves. Nothing else though. No populators.
        // So "carved" is the correct stage to use, not empty. Setting it to empty would clobber chunk data, when we don't
        // need to. If it is populated, at least set it to decorated. If it is lit and populated, set it to mobs_spawned. Else,
        // it is carved.
        // This change also fixes the random light check "bug" (really this is Mojang's fault for fucking up the status conversion here)
        // caused by spigot, which would not set the lit value for some chunks. Now those chunks will not be regenerated.

        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> level = data.getMap("Level");
                if (level == null) {
                    return null;
                }

                final boolean terrainPopulated = level.getBoolean("TerrainPopulated");
                final boolean lightPopulated = level.getBoolean("LightPopulated") || level.getNumber("LightPopulated") == null;
                final String newStatus = !terrainPopulated ? "carved" : (lightPopulated ? "mobs_spawned" : "decorated");

                level.setString("Status", newStatus);
                level.setBoolean("hasLegacyStructureData", true);

                // convert biome byte[] into int[]
                final byte[] biomes = level.getBytes("Biomes");
                if (biomes != null) {
                    final int[] newBiomes = new int[256];
                    for (int i = 0, len = Math.min(newBiomes.length, biomes.length); i < len; ++i) {
                        newBiomes[i] = biomes[i] & 255;
                    }
                    level.setInts("Biomes", newBiomes);
                }

                // ProtoChunks have their own dedicated tick list, so we must convert the TileTicks to that.
                final ListType ticks = level.getList("TileTicks", ObjectType.MAP);
                if (ticks != null) {
                    final ListType sections = Types.NBT.createEmptyList();
                    final ListType[] sectionAccess = new ListType[16];
                    for (int i = 0; i < sectionAccess.length; ++i) {
                        sections.addList(sectionAccess[i] = Types.NBT.createEmptyList());
                    }
                    level.setList("ToBeTicked", sections);

                    for (int i = 0, len = ticks.size(); i < len; ++i) {
                        final MapType<String> tick = ticks.getMap(i);

                        final int x = tick.getInt("x");
                        final int y = tick.getInt("y");
                        final int z = tick.getInt("z");
                        final short coordinate = packOffsetCoordinates(x, y, z);

                        sectionAccess[y >> 4].addShort(coordinate);
                    }
                }

                return null;
            }
        });


        MCTypeRegistry.CHUNK.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final MapType<String> level = data.getMap("Level");
            if (level == null) {
                return null;
            }

            WalkerUtils.convertList(MCTypeRegistry.ENTITY, level, "Entities", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.TILE_ENTITY, level, "TileEntities", fromVersion, toVersion);

            final ListType tileTicks = level.getList("TileTicks", ObjectType.MAP);
            if (tileTicks != null) {
                for (int i = 0, len = tileTicks.size(); i < len; ++i) {
                    final MapType<String> tileTick = tileTicks.getMap(i);
                    WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, tileTick, "i", fromVersion, toVersion);
                }
            }

            final ListType sections = level.getList("Sections", ObjectType.MAP);
            if (sections != null) {
                for (int i = 0, len = sections.size(); i < len; ++i) {
                    final MapType<String> section = sections.getMap(i);
                    WalkerUtils.convertList(MCTypeRegistry.BLOCK_STATE, section, "Palette", fromVersion, toVersion);
                }
            }

            WalkerUtils.convertValues(MCTypeRegistry.STRUCTURE_FEATURE, level.getMap("Structures"), "Starts", fromVersion, toVersion);

            return null;
        });
        MCTypeRegistry.STRUCTURE_FEATURE.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final ListType list = data.getList("Children", ObjectType.MAP);
            if (list != null) {
                for (int i = 0, len = list.size(); i < len; ++i) {
                    final MapType<String> child = list.getMap(i);

                    WalkerUtils.convert(MCTypeRegistry.BLOCK_STATE, child, "CA", fromVersion, toVersion);
                    WalkerUtils.convert(MCTypeRegistry.BLOCK_STATE, child, "CB", fromVersion, toVersion);
                    WalkerUtils.convert(MCTypeRegistry.BLOCK_STATE, child, "CC", fromVersion, toVersion);
                    WalkerUtils.convert(MCTypeRegistry.BLOCK_STATE, child, "CD", fromVersion, toVersion);
                }
            }

            WalkerUtils.convert(MCTypeRegistry.BIOME, data, "biome", fromVersion, toVersion);

            return null;
        });
    }

    private V1466() {}

}
