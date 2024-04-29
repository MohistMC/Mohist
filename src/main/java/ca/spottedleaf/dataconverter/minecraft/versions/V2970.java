package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class V2970 {

    protected static final int VERSION = MCVersions.V22W07A + 1;
    private static final Map<String, BiomeRemap> CONVERSION_MAP = new HashMap<>(
            ImmutableMap.<String, BiomeRemap>builder()
                    .put("mineshaft", BiomeRemap.create(Map.of(List.of("minecraft:badlands", "minecraft:eroded_badlands", "minecraft:wooded_badlands"), "minecraft:mineshaft_mesa"), "minecraft:mineshaft"))
                    .put("shipwreck", BiomeRemap.create(Map.of(List.of("minecraft:beach", "minecraft:snowy_beach"), "minecraft:shipwreck_beached"), "minecraft:shipwreck"))
                    .put("ocean_ruin", BiomeRemap.create(Map.of(List.of("minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:deep_lukewarm_ocean"), "minecraft:ocean_ruin_warm"), "minecraft:ocean_ruin_cold"))
                    .put("village", BiomeRemap.create(Map.of(List.of("minecraft:desert"), "minecraft:village_desert", List.of("minecraft:savanna"), "minecraft:village_savanna", List.of("minecraft:snowy_plains"), "minecraft:village_snowy", List.of("minecraft:taiga"), "minecraft:village_taiga"), "minecraft:village_plains"))
                    .put("ruined_portal", BiomeRemap.create(Map.of(List.of("minecraft:desert"), "minecraft:ruined_portal_desert", List.of("minecraft:badlands", "minecraft:eroded_badlands", "minecraft:wooded_badlands", "minecraft:windswept_hills", "minecraft:windswept_forest", "minecraft:windswept_gravelly_hills", "minecraft:savanna_plateau", "minecraft:windswept_savanna", "minecraft:stony_shore", "minecraft:meadow", "minecraft:frozen_peaks", "minecraft:jagged_peaks", "minecraft:stony_peaks", "minecraft:snowy_slopes"), "minecraft:ruined_portal_mountain", List.of("minecraft:bamboo_jungle", "minecraft:jungle", "minecraft:sparse_jungle"), "minecraft:ruined_portal_jungle", List.of("minecraft:deep_frozen_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_ocean", "minecraft:deep_lukewarm_ocean", "minecraft:frozen_ocean", "minecraft:ocean", "minecraft:cold_ocean", "minecraft:lukewarm_ocean", "minecraft:warm_ocean"), "minecraft:ruined_portal_ocean"), "minecraft:ruined_portal")) // Fix MC-248814, ruined_portal_standard->ruined_portal
                    .put("pillager_outpost", BiomeRemap.create("minecraft:pillager_outpost"))
                    .put("mansion", BiomeRemap.create("minecraft:mansion"))
                    .put("jungle_pyramid", BiomeRemap.create("minecraft:jungle_pyramid"))
                    .put("desert_pyramid", BiomeRemap.create("minecraft:desert_pyramid"))
                    .put("igloo", BiomeRemap.create("minecraft:igloo"))
                    .put("swamp_hut", BiomeRemap.create("minecraft:swamp_hut"))
                    .put("stronghold", BiomeRemap.create("minecraft:stronghold"))
                    .put("monument", BiomeRemap.create("minecraft:monument"))
                    .put("fortress", BiomeRemap.create("minecraft:fortress"))
                    .put("endcity", BiomeRemap.create("minecraft:end_city"))
                    .put("buried_treasure", BiomeRemap.create("minecraft:buried_treasure"))
                    .put("nether_fossil", BiomeRemap.create("minecraft:nether_fossil"))
                    .put("bastion_remnant", BiomeRemap.create("minecraft:bastion_remnant"))
                    .build()
    );

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            private static Object2IntOpenHashMap<String> countBiomes(final MapType<String> chunk) {
                final ListType sections = chunk.getList("sections", ObjectType.MAP);
                if (sections == null) {
                    return null;
                }

                final Object2IntOpenHashMap<String> ret = new Object2IntOpenHashMap<>();

                for (int i = 0, len = sections.size(); i < len; ++i) {
                    final MapType<String> section = sections.getMap(i);

                    final MapType<String> biomes = section.getMap("biomes");

                    if (biomes == null) {
                        continue;
                    }

                    final ListType palette = biomes.getList("palette", ObjectType.STRING);

                    if (palette == null) {
                        continue;
                    }

                    for (int k = 0, len2 = palette.size(); k < len2; ++k) {
                        ret.addTo(palette.getString(k), 1);
                    }
                }

                return ret;
            }

            private static String getStructureConverted(String id, final Object2IntOpenHashMap<String> biomeCount) {
                id = id.toLowerCase(Locale.ROOT);
                final BiomeRemap remap = CONVERSION_MAP.get(id);
                if (remap == null) {
                    throw new IllegalArgumentException("Unknown structure " + id);
                }
                if (remap.biomeToNewStructure == null || biomeCount == null) {
                    return remap.dfl;
                }

                final Object2IntOpenHashMap<String> remapCount = new Object2IntOpenHashMap<>();

                for (final Iterator<Object2IntMap.Entry<String>> iterator = biomeCount.object2IntEntrySet().fastIterator(); iterator.hasNext();) {
                    final Object2IntMap.Entry<String> entry = iterator.next();
                    final String remappedStructure = remap.biomeToNewStructure.get(entry.getKey());
                    if (remappedStructure != null) {
                        remapCount.addTo(remappedStructure, entry.getIntValue());
                    }
                }

                String converted = remap.dfl;
                int maxCount = 0;

                for (final Iterator<Object2IntMap.Entry<String>> iterator = remapCount.object2IntEntrySet().fastIterator(); iterator.hasNext();) {
                    final Object2IntMap.Entry<String> entry = iterator.next();
                    final int count = entry.getIntValue();
                    if (count > maxCount) {
                        maxCount = count;
                        converted = entry.getKey();
                    }
                }

                return converted;
            }

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> structures = data.getMap("structures");
                if (structures == null || structures.isEmpty()) {
                    return null;
                }

                final Object2IntOpenHashMap<String> biomeCounts = countBiomes(data);

                final MapType<String> starts = structures.getMap("starts");
                final MapType<String> references = structures.getMap("References");

                if (starts != null) {
                    final MapType<String> newStarts = Types.NBT.createEmptyMap();
                    structures.setMap("starts", newStarts);

                    for (final String key : starts.keys()) {
                        final MapType<String> value = starts.getMap(key);
                        if ("INVALID".equals(value.getString("id", "INVALID"))) {
                            continue;
                        }

                        final String remapped = getStructureConverted(key, biomeCounts);
                        value.setString("id", remapped);
                        newStarts.setMap(remapped, value);
                    }
                }

                // This TRULY is a guess, no idea what biomes the referent has.
                if (references != null) {
                    final MapType<String> newReferences = Types.NBT.createEmptyMap();
                    structures.setMap("References", newReferences);
                    for (final String key : references.keys()) {
                        final long[] value = references.getLongs(key);
                        if (value.length == 0) {
                            continue;
                        }

                        newReferences.setLongs(getStructureConverted(key, biomeCounts), value);
                    }
                }

                return null;
            }
        });
    }

    private static final class BiomeRemap {

        public final Map<String, String> biomeToNewStructure;
        public final String dfl;

        private BiomeRemap(final Map<String, String> biomeToNewStructure, final String dfl) {
            this.biomeToNewStructure = biomeToNewStructure;
            this.dfl = dfl;
        }

        public static BiomeRemap create(final String newId) {
            return new BiomeRemap(null, newId);
        }

        public static BiomeRemap create(final Map<List<String>, String> biomeMap, final String newId) {
            final Map<String, String> biomeToNewStructure = new HashMap<>();

            for (final Map.Entry<List<String>, String> entry : biomeMap.entrySet()) {
                final List<String> biomes = entry.getKey();
                final String newBiomeStructure = entry.getValue();

                for (int i = 0, len = biomes.size(); i < len; ++i) {
                    final String biome = biomes.get(i);
                    if (biomeToNewStructure.putIfAbsent(biome, newBiomeStructure) != null) {
                        throw new IllegalStateException("Duplicate biome remap: " + biome + " -> " + newBiomeStructure + ", but already mapped to " + biomeToNewStructure.get(biome));
                    }
                }
            }

            return new BiomeRemap(biomeToNewStructure, newId);
        }
    }
}
