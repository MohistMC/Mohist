package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.math.NumberUtils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class V2550 {

    protected static final int VERSION = MCVersions.V20W20B + 13;

    private static final ImmutableMap<String, StructureFeatureConfiguration> DEFAULTS = ImmutableMap.<String, StructureFeatureConfiguration>builder()
            .put("minecraft:village", new StructureFeatureConfiguration(32, 8, 10387312))
            .put("minecraft:desert_pyramid", new StructureFeatureConfiguration(32, 8, 14357617))
            .put("minecraft:igloo", new StructureFeatureConfiguration(32, 8, 14357618))
            .put("minecraft:jungle_pyramid", new StructureFeatureConfiguration(32, 8, 14357619))
            .put("minecraft:swamp_hut", new StructureFeatureConfiguration(32, 8, 14357620))
            .put("minecraft:pillager_outpost", new StructureFeatureConfiguration(32, 8, 165745296))
            .put("minecraft:monument", new StructureFeatureConfiguration(32, 5, 10387313))
            .put("minecraft:endcity", new StructureFeatureConfiguration(20, 11, 10387313))
            .put("minecraft:mansion", new StructureFeatureConfiguration(80, 20, 10387319))
            .build();

    record StructureFeatureConfiguration(int spacing, int separation, int salt) {

        public MapType<String> serialize() {
            final MapType<String> ret = Types.NBT.createEmptyMap();

            ret.setInt("spacing", this.spacing);
            ret.setInt("separation", this.separation);
            ret.setInt("salt", this.salt);

            return ret;
        }
    }

    public static void register() {
        MCTypeRegistry.WORLD_GEN_SETTINGS.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final long seed = data.getLong("RandomSeed");
                String generatorName = data.getString("generatorName");
                if (generatorName != null) {
                    generatorName = generatorName.toLowerCase(Locale.ROOT);
                }
                String legacyCustomOptions = data.getString("legacy_custom_options");
                if (legacyCustomOptions == null) {
                    legacyCustomOptions = "customized".equals(generatorName) ? data.getString("generatorOptions") : null;
                }

                final MapType<String> generator;
                boolean caves = false;

                if ("customized".equals(generatorName) || generatorName == null) {
                    generator = defaultOverworld(seed);
                } else {
                    switch (generatorName) {
                        case "flat": {
                            final MapType<String> generatorOptions = data.getMap("generatorOptions");

                            final MapType<String> structures = fixFlatStructures(generatorOptions);
                            final MapType<String> settings = Types.NBT.createEmptyMap();
                            generator = Types.NBT.createEmptyMap();
                            generator.setString("type", "minecraft:flat");
                            generator.setMap("settings", settings);

                            settings.setMap("structures", structures);

                            ListType layers = generatorOptions.getList("layers", ObjectType.MAP);
                            if (layers == null) {
                                layers = Types.NBT.createEmptyList();

                                final int[] heights = new int[] { 1, 2, 1 };
                                final String[] blocks = new String[] { "minecraft:bedrock", "minecraft:dirt", "minecraft:grass_block" };
                                for (int i = 0; i < 3; ++i) {
                                    final MapType<String> layer = Types.NBT.createEmptyMap();
                                    layer.setInt("height", heights[i]);
                                    layer.setString("block", blocks[i]);
                                    layers.addMap(layer);
                                }
                            }

                            settings.setList("layers", layers);
                            settings.setString("biome", generatorOptions.getString("biome", "minecraft:plains"));

                            break;
                        }

                        case "debug_all_block_states": {
                            generator = Types.NBT.createEmptyMap();
                            generator.setString("type", "minecraft:debug");
                            break;
                        }

                        case "buffet": {
                            final MapType<String> generatorOptions = data.getMap("generatorOptions");
                            final MapType<String> chunkGenerator = generatorOptions == null ? null : generatorOptions.getMap("chunk_generator");
                            final String chunkGeneratorType = chunkGenerator == null ? null  : chunkGenerator.getString("type");

                            final String newType;
                            if ("minecraft:caves".equals(chunkGeneratorType)) {
                                newType = "minecraft:caves";
                                caves = true;
                            } else if ("minecraft:floating_islands".equals(chunkGeneratorType)) {
                                newType = "minecraft:floating_islands";
                            } else {
                                newType = "minecraft:overworld";
                            }

                            MapType<String> biomeSource = generatorOptions == null ? null : generatorOptions.getMap("biome_source");
                            if (biomeSource == null) {
                                biomeSource = Types.NBT.createEmptyMap();
                                biomeSource.setString("type", "minecraft:fixed");
                            }

                            if ("minecraft:fixed".equals(biomeSource.getString("type"))) {
                                final MapType<String> options = biomeSource.getMap("options");
                                final ListType biomes = options == null ? null : options.getList("biomes", ObjectType.STRING);
                                final String biome = biomes == null || biomes.size() == 0 ? "minecraft:ocean" : biomes.getString(0);
                                biomeSource.remove("options");
                                biomeSource.setString("biome", biome);
                            }

                            generator = noise(seed, newType, biomeSource);
                            break;
                        }

                        default: {
                            boolean defaultGen = generatorName.equals("default");
                            boolean default11Gen = generatorName.equals("default_1_1") || defaultGen && data.getInt("generatorVersion") == 0;
                            boolean amplified = generatorName.equals("amplified");
                            boolean largeBiomes = generatorName.equals("largebiomes");

                            generator = noise(seed, amplified ? "minecraft:amplified" : "minecraft:overworld",
                                    vanillaBiomeSource(seed, default11Gen, largeBiomes));
                            break;
                        }
                    }
                }

                final boolean mapFeatures = data.getBoolean("MapFeatures", true);
                final boolean bonusChest = data.getBoolean("BonusChest", false);

                final MapType<String> ret = Types.NBT.createEmptyMap();

                ret.setLong("seed", seed);
                ret.setBoolean("generate_features", mapFeatures);
                ret.setBoolean("bonus_chest", bonusChest);
                ret.setMap("dimensions", vanillaLevels(seed, generator, caves));
                if (legacyCustomOptions != null) {
                    ret.setString("legacy_custom_options", legacyCustomOptions);
                }

                return ret;
            }
        });
    }

    public static MapType<String> noise(final long seed, final String worldType, final MapType<String> biomeSource) {
        final MapType<String> ret = Types.NBT.createEmptyMap();

        ret.setString("type", "minecraft:noise");
        ret.setMap("biome_source", biomeSource);
        ret.setLong("seed", seed);
        ret.setString("settings", worldType);

        return ret;
    }

    public static MapType<String> vanillaBiomeSource(final long seed, final boolean default11Gen, final boolean largeBiomes) {
        final MapType<String> ret = Types.NBT.createEmptyMap();

        ret.setString("type", "minecraft:vanilla_layered");
        ret.setLong("seed", seed);
        ret.setBoolean("large_biomes", largeBiomes);
        if (default11Gen) {
            ret.setBoolean("legacy_biome_init_layer", default11Gen);
        }

        return ret;
    }

    public static MapType<String> fixFlatStructures(final MapType<String> generatorOptions) {
        int distance = 32;
        int spread = 3;
        int count = 128;
        boolean stronghold = false;
        final Map<String, StructureFeatureConfiguration> newStructures = new HashMap<>();

        if (generatorOptions == null) {
            stronghold = true;
            newStructures.put("minecraft:village", DEFAULTS.get("minecraft:village"));
        }

        final MapType<String> oldStructures = generatorOptions == null ? null : generatorOptions.getMap("structures");
        if (oldStructures != null) {
            for (final String structureName : oldStructures.keys()) {
                final MapType<String> structureValues = oldStructures.getMap(structureName);
                if (structureValues == null) {
                    continue;
                }

                for (final String structureValueKey : structureValues.keys()) {
                    final String structureValue = structureValues.getString(structureValueKey);

                    if ("stronghold".equals(structureName)) {
                        stronghold = true;
                        switch (structureValueKey) {
                            case "distance":
                                distance = getInt(structureValue, distance, 1);
                                break;
                            case "spread":
                                spread = getInt(structureValue, spread, 1);
                                break;
                            case "count":
                                count = getInt(structureValue, count, 1);
                                break;
                        }
                    } else {
                        switch (structureValueKey) {
                            case "distance":
                                switch (structureName) {
                                    case "village":
                                        setSpacing(newStructures, "minecraft:village", structureValue, 9);
                                        break;
                                    case "biome_1":
                                        setSpacing(newStructures, "minecraft:desert_pyramid", structureValue, 9);
                                        setSpacing(newStructures, "minecraft:igloo", structureValue, 9);
                                        setSpacing(newStructures, "minecraft:jungle_pyramid", structureValue, 9);
                                        setSpacing(newStructures, "minecraft:swamp_hut", structureValue, 9);
                                        setSpacing(newStructures, "minecraft:pillager_outpost", structureValue, 9);
                                        break;
                                    case "endcity":
                                        setSpacing(newStructures, "minecraft:endcity", structureValue, 1);
                                        break;
                                    case "mansion":
                                        setSpacing(newStructures, "minecraft:mansion", structureValue, 1);
                                        break;
                                    default:
                                        break;
                                }
                            case "separation":
                                if ("oceanmonument".equals(structureName)) {
                                    final StructureFeatureConfiguration structure = newStructures.getOrDefault("minecraft:monument", DEFAULTS.get("minecraft:monument"));
                                    final int newSpacing = getInt(structureValue, structure.separation, 1);
                                    newStructures.put("minecraft:monument", new StructureFeatureConfiguration(newSpacing, structure.separation, structure.salt));
                                }

                                break;
                            case "spacing":
                                if ("oceanmonument".equals(structureName)) {
                                    setSpacing(newStructures, "minecraft:monument", structureValue, 1);
                                }

                                break;
                        }
                    }
                }
            }
        }

        final MapType<String> ret = Types.NBT.createEmptyMap();
        final MapType<String> structuresSerialized = Types.NBT.createEmptyMap();
        ret.setMap("structures", structuresSerialized);
        for (final String key : newStructures.keySet()) {
            structuresSerialized.setMap(key, newStructures.get(key).serialize());
        }

        if (stronghold) {
            final MapType<String> strongholdData = Types.NBT.createEmptyMap();
            ret.setMap("stronghold", strongholdData);

            strongholdData.setInt("distance", distance);
            strongholdData.setInt("spread", spread);
            strongholdData.setInt("count", count);
        }

        return ret;
    }

    public static MapType<String> vanillaLevels(final long seed, final MapType<String> generator, final boolean caves) {
        final MapType<String> ret = Types.NBT.createEmptyMap();

        final MapType<String> overworld = Types.NBT.createEmptyMap();
        final MapType<String> nether = Types.NBT.createEmptyMap();
        final MapType<String> end = Types.NBT.createEmptyMap();

        ret.setMap("minecraft:overworld", overworld);
        ret.setMap("minecraft:the_nether", nether);
        ret.setMap("minecraft:the_end", end);

        // overworld
        overworld.setString("type", caves ? "minecraft:overworld_caves" : "minecraft:overworld");
        overworld.setMap("generator", generator);

        // nether
        nether.setString("type", "minecraft:the_nether");
        final MapType<String> netherBiomeSource = Types.NBT.createEmptyMap();
        netherBiomeSource.setString("type", "minecraft:multi_noise");
        netherBiomeSource.setLong("seed", seed);
        netherBiomeSource.setString("preset", "minecraft:nether");

        nether.setMap("generator", noise(seed, "minecraft:nether", netherBiomeSource));

        // end
        end.setString("type", "minecraft:the_end");
        final MapType<String> endBiomeSource = Types.NBT.createEmptyMap();
        endBiomeSource.setString("type", "minecraft:the_end");
        endBiomeSource.setLong("seed", seed);
        end.setMap("generator", noise(seed,"minecraft:end", endBiomeSource));

        return ret;
    }

    public static MapType<String> defaultOverworld(final long seed) {
        return noise(seed, "minecraft:overworld", vanillaBiomeSource(seed, false, false));
    }

    private static int getInt(final String value, final int dfl) {
        return NumberUtils.toInt(value, dfl);
    }

    private static int getInt(final String value, final int dfl, final int minVal) {
        return Math.max(minVal, getInt(value, dfl));
    }

    private static void setSpacing(final Map<String, StructureFeatureConfiguration> structures, final String structureName,
                                   final String value, final int minVal) {
        final StructureFeatureConfiguration structure = structures.getOrDefault(structureName, DEFAULTS.get(structureName));
        final int newSpacing = getInt(value, structure.spacing, minVal);

        structures.put(structureName, new StructureFeatureConfiguration(newSpacing, structure.separation, structure.salt));
    }
}
