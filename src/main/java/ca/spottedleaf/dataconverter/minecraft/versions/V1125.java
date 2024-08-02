package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.hooks.DataHookValueTypeEnforceNamespaced;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.Types;

public final class V1125 {

    protected static final int VERSION = MCVersions.V17W15A;
    protected static final int BED_BLOCK_ID = 416;

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> level = data.getMap("Level");
                if (level == null) {
                    return null;
                }

                final int chunkX = level.getInt("xPos");
                final int chunkZ = level.getInt("zPos");

                final ListType sections = level.getList("Sections", ObjectType.MAP);
                if (sections == null) {
                    return null;
                }

                ListType tileEntities = level.getList("TileEntities", ObjectType.MAP);
                if (tileEntities == null) {
                    tileEntities = Types.NBT.createEmptyList();
                    level.setList("TileEntities", tileEntities);
                }

                for (int i = 0, len = sections.size(); i < len; ++i) {
                    final MapType<String> section = sections.getMap(i);

                    final byte sectionY = section.getByte("Y");
                    final byte[] blocks = section.getBytes("Blocks");

                    if (blocks == null) {
                        continue;
                    }

                    for (int blockIndex = 0; blockIndex < blocks.length; ++blockIndex) {
                        if (BED_BLOCK_ID != ((blocks[blockIndex] & 255) << 4)) {
                            continue;
                        }

                        final int localX = blockIndex & 15;
                        final int localZ = (blockIndex >> 4) & 15;
                        final int localY = (blockIndex >> 8) & 15;

                        final MapType<String> newTile = Types.NBT.createEmptyMap();
                        newTile.setString("id", "minecraft:bed");
                        newTile.setInt("x", localX + (chunkX << 4));
                        newTile.setInt("y", localY + (sectionY << 4));
                        newTile.setInt("z", localZ + (chunkZ << 4));
                        newTile.setShort("color", (short)14); // Red

                        tileEntities.addMap(newTile);
                    }
                }

                return null;
            }
        });

        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:bed", new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (data.getShort("Damage") == 0) {
                    data.setShort("Damage", (short)14); // Red
                }

                return null;
            }
        });


        MCTypeRegistry.ADVANCEMENTS.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convertKeys(MCTypeRegistry.BIOME, data.getMap("minecraft:adventure/adventuring_time"), "criteria", fromVersion, toVersion);
            WalkerUtils.convertKeys(MCTypeRegistry.ENTITY_NAME, data.getMap("minecraft:adventure/kill_a_mob"), "criteria", fromVersion, toVersion);
            WalkerUtils.convertKeys(MCTypeRegistry.ENTITY_NAME, data.getMap("minecraft:adventure/kill_all_mobs"), "criteria", fromVersion, toVersion);
            WalkerUtils.convertKeys(MCTypeRegistry.ENTITY_NAME, data.getMap("minecraft:adventure/bred_all_animals"), "criteria", fromVersion, toVersion);

            return null;
        });

        // Enforce namespacing for ids
        MCTypeRegistry.BIOME.addStructureHook(VERSION, new DataHookValueTypeEnforceNamespaced());
    }

    private V1125() {}

}
