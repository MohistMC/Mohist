package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.json.JsonMapType;
import ca.spottedleaf.dataconverter.types.json.JsonTypeUtil;
import ca.spottedleaf.dataconverter.types.nbt.NBTMapType;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public final class V1506 {

    protected static final int VERSION = MCVersions.V1_13_PRE4 + 2;

    static final Map<String, String> MAP = new HashMap<>();
    static {
        MAP.put("0", "minecraft:ocean");
        MAP.put("1", "minecraft:plains");
        MAP.put("2", "minecraft:desert");
        MAP.put("3", "minecraft:mountains");
        MAP.put("4", "minecraft:forest");
        MAP.put("5", "minecraft:taiga");
        MAP.put("6", "minecraft:swamp");
        MAP.put("7", "minecraft:river");
        MAP.put("8", "minecraft:nether");
        MAP.put("9", "minecraft:the_end");
        MAP.put("10", "minecraft:frozen_ocean");
        MAP.put("11", "minecraft:frozen_river");
        MAP.put("12", "minecraft:snowy_tundra");
        MAP.put("13", "minecraft:snowy_mountains");
        MAP.put("14", "minecraft:mushroom_fields");
        MAP.put("15", "minecraft:mushroom_field_shore");
        MAP.put("16", "minecraft:beach");
        MAP.put("17", "minecraft:desert_hills");
        MAP.put("18", "minecraft:wooded_hills");
        MAP.put("19", "minecraft:taiga_hills");
        MAP.put("20", "minecraft:mountain_edge");
        MAP.put("21", "minecraft:jungle");
        MAP.put("22", "minecraft:jungle_hills");
        MAP.put("23", "minecraft:jungle_edge");
        MAP.put("24", "minecraft:deep_ocean");
        MAP.put("25", "minecraft:stone_shore");
        MAP.put("26", "minecraft:snowy_beach");
        MAP.put("27", "minecraft:birch_forest");
        MAP.put("28", "minecraft:birch_forest_hills");
        MAP.put("29", "minecraft:dark_forest");
        MAP.put("30", "minecraft:snowy_taiga");
        MAP.put("31", "minecraft:snowy_taiga_hills");
        MAP.put("32", "minecraft:giant_tree_taiga");
        MAP.put("33", "minecraft:giant_tree_taiga_hills");
        MAP.put("34", "minecraft:wooded_mountains");
        MAP.put("35", "minecraft:savanna");
        MAP.put("36", "minecraft:savanna_plateau");
        MAP.put("37", "minecraft:badlands");
        MAP.put("38", "minecraft:wooded_badlands_plateau");
        MAP.put("39", "minecraft:badlands_plateau");
        MAP.put("40", "minecraft:small_end_islands");
        MAP.put("41", "minecraft:end_midlands");
        MAP.put("42", "minecraft:end_highlands");
        MAP.put("43", "minecraft:end_barrens");
        MAP.put("44", "minecraft:warm_ocean");
        MAP.put("45", "minecraft:lukewarm_ocean");
        MAP.put("46", "minecraft:cold_ocean");
        MAP.put("47", "minecraft:deep_warm_ocean");
        MAP.put("48", "minecraft:deep_lukewarm_ocean");
        MAP.put("49", "minecraft:deep_cold_ocean");
        MAP.put("50", "minecraft:deep_frozen_ocean");
        MAP.put("127", "minecraft:the_void");
        MAP.put("129", "minecraft:sunflower_plains");
        MAP.put("130", "minecraft:desert_lakes");
        MAP.put("131", "minecraft:gravelly_mountains");
        MAP.put("132", "minecraft:flower_forest");
        MAP.put("133", "minecraft:taiga_mountains");
        MAP.put("134", "minecraft:swamp_hills");
        MAP.put("140", "minecraft:ice_spikes");
        MAP.put("149", "minecraft:modified_jungle");
        MAP.put("151", "minecraft:modified_jungle_edge");
        MAP.put("155", "minecraft:tall_birch_forest");
        MAP.put("156", "minecraft:tall_birch_hills");
        MAP.put("157", "minecraft:dark_forest_hills");
        MAP.put("158", "minecraft:snowy_taiga_mountains");
        MAP.put("160", "minecraft:giant_spruce_taiga");
        MAP.put("161", "minecraft:giant_spruce_taiga_hills");
        MAP.put("162", "minecraft:modified_gravelly_mountains");
        MAP.put("163", "minecraft:shattered_savanna");
        MAP.put("164", "minecraft:shattered_savanna_plateau");
        MAP.put("165", "minecraft:eroded_badlands");
        MAP.put("166", "minecraft:modified_wooded_badlands_plateau");
        MAP.put("167", "minecraft:modified_badlands_plateau");
    }

    private V1506() {}

    public static void register() {
        MCTypeRegistry.LEVEL.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final String generatorOptions = data.getString("generatorOptions");
                final String generatorName = data.getString("generatorName");
                if ("flat".equalsIgnoreCase(generatorName)) {
                    data.setMap("generatorOptions", V1506.convert(generatorOptions == null ? "" : generatorOptions));
                } else if ("buffet".equalsIgnoreCase(generatorName) && generatorOptions != null) {
                    data.setMap("generatorOptions", JsonTypeUtil.convertJsonToNBT(new JsonMapType(GsonHelper.parse(generatorOptions, true), false)));
                }
                return null;
            }
        });
    }

    private static MapType<String> convert(final String param0) {
        final Dynamic<Tag> dynamic = convert(param0, NbtOps.INSTANCE);

        return new NBTMapType((CompoundTag)dynamic.getValue());
    }

    // Yeah I ain't touching that. This is basically magic value hell.
    private static <T> Dynamic<T> convert(final String generatorSettings, final DynamicOps<T> ops) {
        final Iterator<String> splitSettings = Splitter.on(';').split(generatorSettings).iterator();
        String biome = "minecraft:plains";
        final Map<String, Map<String, String>> structures = Maps.newHashMap();
        final List<Pair<Integer, String>> layers;
        if (!generatorSettings.isEmpty() && splitSettings.hasNext()) {
            layers = getLayersInfoFromString(splitSettings.next());
            if (!layers.isEmpty()) {
                // biome is next
                if (splitSettings.hasNext()) {
                    biome = MAP.getOrDefault(splitSettings.next(), "minecraft:plains");
                }

                // structures is next
                if (splitSettings.hasNext()) {
                    final String[] structuresSplit = splitSettings.next().toLowerCase(Locale.ROOT).split(",");

                    for (final String structureString : structuresSplit) {
                        final String[] structureInfo = structureString.split("\\(", 2);
                        if (!structureInfo[0].isEmpty()) {
                            structures.put(structureInfo[0], Maps.newHashMap());
                            if (structureInfo.length > 1 && structureInfo[1].endsWith(")") && structureInfo[1].length() > 1) {
                                // I can't even guess the mappings for these. Not worth my time, it will work regardless of the mappings
                                final String[] var7 = structureInfo[1].substring(0, structureInfo[1].length() - 1).split(" ");

                                for (final String var8 : var7) {
                                    String[] var9 = var8.split("=", 2);
                                    if (var9.length == 2) {
                                        structures.get(structureInfo[0]).put(var9[0], var9[1]);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    structures.put("village", Maps.newHashMap());
                }
            }
        } else {
            layers = Lists.newArrayList();
            layers.add(Pair.of(1, "minecraft:bedrock"));
            layers.add(Pair.of(2, "minecraft:dirt"));
            layers.add(Pair.of(1, "minecraft:grass_block"));
            structures.put("village", Maps.newHashMap());
        }

        final T layerTag = ops.createList(layers.stream().map((param1x) -> ops.createMap(ImmutableMap.of(ops.createString("height"), ops.createInt(param1x.getFirst()), ops.createString("block"), ops.createString(param1x.getSecond())))));
        final T structuresTag = ops.createMap(structures.entrySet().stream().map((param1x) -> Pair.of(ops.createString(param1x.getKey().toLowerCase(Locale.ROOT)), ops.createMap(param1x.getValue().entrySet().stream().map((param1xx) -> Pair.of(ops.createString(param1xx.getKey()), ops.createString(param1xx.getValue()))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond))))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("layers"), layerTag, ops.createString("biome"), ops.createString(biome), ops.createString("structures"), structuresTag)));
    }

    private static Pair<Integer, String> getLayerInfoFromString(final String layerString) {
        final String[] split = layerString.split("\\*", 2);
        int layerCount;
        if (split.length == 2) {
            try {
                layerCount = Integer.parseInt(split[0]);
            } catch (final NumberFormatException ex) {
                return null;
            }
        } else {
            layerCount = 1;
        }

        final String blockName = split[split.length - 1];
        return Pair.of(layerCount, blockName);
    }

    private static List<Pair<Integer, String>> getLayersInfoFromString(final String layersString) {
        final List<Pair<Integer, String>> ret = new ArrayList<>();
        final String[] layers = layersString.split(",");

        for (final String layerString : layers) {
            final Pair<Integer, String> layer = getLayerInfoFromString(layerString);
            if (layer == null) {
                return Collections.emptyList();
            }

            ret.add(layer);
        }

        return ret;
    }
}
