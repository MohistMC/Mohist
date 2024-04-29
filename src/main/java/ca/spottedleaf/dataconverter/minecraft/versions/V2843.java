package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.ConverterAbstractStringValueTypeRename;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;
import java.util.Map;

public final class V2843 {

    protected static final int VERSION = MCVersions.V21W42A + 3;

    public static void register() {
        ConverterAbstractStringValueTypeRename.register(VERSION, MCTypeRegistry.BIOME, Map.of("minecraft:deep_warm_ocean", "minecraft:warm_ocean")::get);

        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            private static void moveOutOfBoundTicks(final ListType ticks, final MapType<String> chunkRoot, final int chunkX, final int chunkZ, final String intoKey) {
                if (ticks == null) {
                    return;
                }

                ListType outOfBounds = null;
                for (int i = 0, len = ticks.size(); i < len; ++i) {
                    final MapType<String> tick = ticks.getMap(i);
                    final int x = tick.getInt("x");
                    final int z = tick.getInt("z");
                    // anything > 1 is lost, anything less than 1 stays.
                    if (Math.max(Math.abs(chunkX - (x >> 4)), Math.abs(chunkZ - (z >> 4))) == 1) {
                        // DFU doesn't remove, so neither do we.
                        if (outOfBounds == null) {
                            outOfBounds = Types.NBT.createEmptyList();
                        }
                        outOfBounds.addMap(tick.copy());
                    }
                }

                if (outOfBounds != null) {
                    MapType<String> upgradeData = chunkRoot.getMap("UpgradeData");
                    if (upgradeData == null) {
                        chunkRoot.setMap("UpgradeData", upgradeData = Types.NBT.createEmptyMap());
                    }
                    upgradeData.setList(intoKey, outOfBounds);
                }
            }

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                // After renames, so use new names
                final int x = data.getInt("xPos");
                final int z = data.getInt("zPos");

                moveOutOfBoundTicks(data.getList("block_ticks", ObjectType.MAP), data, x, z, "neighbor_block_ticks");
                moveOutOfBoundTicks(data.getList("fluid_ticks", ObjectType.MAP), data, x, z, "neighbor_fluid_ticks");

                return null;
            }
        });

        // DFU is missing schema for UpgradeData block names
        MCTypeRegistry.CHUNK.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convertList(MCTypeRegistry.ENTITY, data, "entities", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.TILE_ENTITY, data, "block_entities", fromVersion, toVersion);

            final ListType blockTicks = data.getList("block_ticks", ObjectType.MAP);
            if (blockTicks != null) {
                for (int i = 0, len = blockTicks.size(); i < len; ++i) {
                    WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, blockTicks.getMap(i), "i", fromVersion, toVersion);
                }
            }

            final MapType<String> upgradeData = data.getMap("UpgradeData");
            if (upgradeData != null) {
                // Even though UpgradeData will retrieve the block from the World when the type no longer exists,
                // the type from the world may not match what was actually queued. So, even though it may look like we
                // can skip the walker here, we actually don't if we want to be thorough.
                final ListType neighbourBlockTicks = upgradeData.getList("neighbor_block_ticks", ObjectType.MAP);
                if (neighbourBlockTicks != null) {
                    for (int i = 0, len = neighbourBlockTicks.size(); i < len; ++i) {
                        WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, neighbourBlockTicks.getMap(i), "i", fromVersion, toVersion);
                    }
                }
            }

            final ListType sections = data.getList("sections", ObjectType.MAP);
            if (sections != null) {
                for (int i = 0, len = sections.size(); i < len; ++i) {
                    final MapType<String> section = sections.getMap(i);

                    WalkerUtils.convertList(MCTypeRegistry.BIOME, section.getMap("biomes"), "palette", fromVersion, toVersion);
                    WalkerUtils.convertList(MCTypeRegistry.BLOCK_STATE, section.getMap("block_states"), "palette", fromVersion, toVersion);
                }
            }

            WalkerUtils.convertValues(MCTypeRegistry.STRUCTURE_FEATURE, data.getMap("structures"), "starts", fromVersion, toVersion);

            return null;
        });

    }
}
