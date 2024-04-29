package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.RenameHelper;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;

public final class V2842 {

    protected static final int VERSION = MCVersions.V21W42A + 2;

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> root, final long sourceVersion, final long toVersion) {
                final MapType<String> level = root.getMap("Level");
                root.remove("Level");

                if (!root.isEmpty()) {
                    for (final String key : root.keys()) {
                        if (level.hasKey(key)) {
                            // Don't clobber level's data
                            continue;
                        }
                        level.setGeneric(key, root.getGeneric(key));
                    }
                }

                // Rename top level first
                RenameHelper.renameSingle(level, "TileEntities", "block_entities");
                RenameHelper.renameSingle(level, "TileTicks", "block_ticks");
                RenameHelper.renameSingle(level, "Entities", "entities");
                RenameHelper.renameSingle(level, "Sections", "sections");
                RenameHelper.renameSingle(level, "Structures", "structures");

                // 2nd level
                final MapType<String> structures = level.getMap("structures");
                if (structures != null) {
                    RenameHelper.renameSingle(structures, "Starts", "starts");
                }

                return level; // Level is now root tag.
            }
        });

        MCTypeRegistry.CHUNK.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            WalkerUtils.convertList(MCTypeRegistry.ENTITY, data, "entities", fromVersion, toVersion);
            WalkerUtils.convertList(MCTypeRegistry.TILE_ENTITY, data, "block_entities", fromVersion, toVersion);

            final ListType blockTicks = data.getList("block_ticks", ObjectType.MAP);
            if (blockTicks != null) {
                for (int i = 0, len = blockTicks.size(); i < len; ++i) {
                    WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, blockTicks.getMap(i), "i", fromVersion, toVersion);
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
