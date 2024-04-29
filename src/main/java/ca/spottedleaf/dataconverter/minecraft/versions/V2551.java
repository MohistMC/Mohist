package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;

public final class V2551 {

    protected static final int VERSION = MCVersions.V20W20B + 14;

    public static void register() {
        MCTypeRegistry.WORLD_GEN_SETTINGS.addStructureWalker(VERSION, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final MapType<String> dimensions = data.getMap("dimensions");

            if (dimensions == null) {
                return null;
            }

            for (final String dimension : dimensions.keys()) {
                final MapType<String> dimensionData = dimensions.getMap(dimension);
                if (dimensionData == null) {
                    continue;
                }

                final MapType<String> generator = dimensionData.getMap("generator");
                if (generator == null) {
                    continue;
                }

                final String type = generator.getString("type");
                if (type == null) {
                    continue;
                }

                switch (type) {
                    case "minecraft:flat": {
                        final MapType<String> settings = generator.getMap("settings");
                        if (settings == null) {
                            continue;
                        }

                        WalkerUtils.convert(MCTypeRegistry.BIOME, settings, "biome", fromVersion, toVersion);

                        final ListType layers = settings.getList("layers", ObjectType.MAP);
                        if (layers != null) {
                            for (int i = 0, len = layers.size(); i < len; ++i) {
                                WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, layers.getMap(i), "block", fromVersion, toVersion);
                            }
                        }

                        break;
                    }
                    case "minecraft:noise": {
                        final MapType<String> settings = generator.getMap("settings");
                        if (settings != null) {
                            WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, settings, "default_block", fromVersion, toVersion);
                            WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, settings, "default_fluid", fromVersion, toVersion);
                        }

                        final MapType<String> biomeSource = generator.getMap("biome_source");
                        if (biomeSource != null) {
                            final String biomeSourceType = biomeSource.getString("type", "");
                            switch (biomeSourceType) {
                                case "minecraft:fixed": {
                                    WalkerUtils.convert(MCTypeRegistry.BIOME, biomeSource, "biome", fromVersion, toVersion);
                                    break;
                                }

                                case "minecraft:multi_noise": {
                                    // Vanilla's schema is wrong. It should be DSL.fields("biomes", DSL.list(DSL.fields("biome")))
                                    // But it just contains the list part. That obviously can never be the case, because
                                    // the root object is a compound, not a list.

                                    final ListType biomes = biomeSource.getList("biomes", ObjectType.MAP);
                                    if (biomes != null) {
                                        for (int i = 0, len = biomes.size(); i < len; ++i) {
                                            WalkerUtils.convert(MCTypeRegistry.BIOME, biomes.getMap(i), "biome", fromVersion, toVersion);
                                        }
                                    }
                                    break;
                                }

                                case "minecraft:checkerboard": {
                                    WalkerUtils.convertList(MCTypeRegistry.BIOME, biomeSource, "biomes", fromVersion, toVersion);
                                    break;
                                }
                            }
                        }

                        break;
                    }
                }
            }

            return null;
        });
    }
}
