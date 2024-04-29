package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.converters.datatypes.DataHook;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.chunk.ConverterFlattenChunk;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.HelperBlockFlatteningV1450;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.HelperItemNameV102;
import ca.spottedleaf.dataconverter.minecraft.converters.itemstack.ConverterFlattenItemStack;
import ca.spottedleaf.dataconverter.minecraft.converters.itemstack.ConverterFlattenSpawnEgg;
import ca.spottedleaf.dataconverter.minecraft.converters.stats.ConverterFlattenStats;
import ca.spottedleaf.dataconverter.minecraft.converters.entity.ConverterFlattenEntity;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.DataWalkerTypePaths;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.WalkerUtils;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItems;
import ca.spottedleaf.dataconverter.minecraft.walkers.tile_entity.DataWalkerTileEntities;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;
import com.google.common.base.Splitter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.BlockStateData;
import net.minecraft.util.datafix.fixes.EntityBlockStateFix;
import org.apache.commons.lang3.math.NumberUtils;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class V1451 {

    protected static final int VERSION = MCVersions.V17W47A;

    public static void register() {
        // V0
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, 0, "minecraft:trapped_chest", new DataWalkerItemLists("Items"));

        // V1
        MCTypeRegistry.CHUNK.addStructureConverter(new ConverterFlattenChunk());

        MCTypeRegistry.CHUNK.addStructureWalker(VERSION, 1, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
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

            return null;
        });

        // V2
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:piston", new DataConverter<>(VERSION, 2) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final int blockId = data.getInt("blockId");
                final int blockData = data.getInt("blockData") & 15;

                data.remove("blockId");
                data.remove("blockData");

                data.setMap("blockState", HelperBlockFlatteningV1450.getNBTForId((blockId << 4) | blockData).copy()); // copy to avoid problems with later state datafixers

                return null;
            }
        });

        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, 2, "minecraft:piston", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "blockState"));

        // V3
        ConverterFlattenEntity.register();
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:filled_map", new DataConverter<>(VERSION, 3) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                MapType<String> tag = data.getMap("tag");
                if (tag == null) {
                    tag = Types.NBT.createEmptyMap();
                    data.setMap("tag", tag);
                }

                if (!tag.hasKey("map", ObjectType.NUMBER)) { // This if is from CB. as usual, no documentation from CB. I'm guessing it just wants to avoid possibly overwriting it. seems fine.
                    tag.setInt("map", data.getInt("Damage"));
                }

                return null;
            }
        });

        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:potion", new DataWalkerItems("Potion"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:arrow", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "inBlockState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:enderman", new DataWalkerItemLists("ArmorItems", "HandItems"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:enderman", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "carriedBlockState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:falling_block", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "BlockState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:falling_block", new DataWalkerTileEntities("TileEntityData"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:spectral_arrow", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "inBlockState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:chest_minecart", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "DisplayState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:chest_minecart", new DataWalkerItemLists("Items"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:commandblock_minecart", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "DisplayState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:furnace_minecart", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "DisplayState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:hopper_minecart", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "DisplayState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:hopper_minecart", new DataWalkerItemLists("Items"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:minecart", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "DisplayState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:spawner_minecart", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "DisplayState"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:spawner_minecart", (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            MCTypeRegistry.UNTAGGED_SPAWNER.convert(data, fromVersion, toVersion);
            return null;
        });
        MCTypeRegistry.ENTITY.addWalker(VERSION, 3, "minecraft:tnt_minecart", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "DisplayState"));

        // V4
        MCTypeRegistry.BLOCK_NAME.addConverter(new DataConverter<>(VERSION, 4) {
            @Override
            public Object convert(final Object data, final long sourceVersion, final long toVersion) {
                if (data instanceof Number) {
                    return HelperBlockFlatteningV1450.getNameForId(((Number)data).intValue());
                } else if (data instanceof String) {
                    return HelperBlockFlatteningV1450.getNewBlockName((String)data); // structure hook ensured data is namespaced
                }
                return null;
            }
        });
        MCTypeRegistry.ITEM_STACK.addStructureConverter(new ConverterFlattenItemStack());

        // V5
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:spawn_egg", new ConverterFlattenSpawnEgg(VERSION, 5));
        /* This datafixer has been disabled because the collar colour handler did not change from 1.12 -> 1.13 at all.
        // So clearly somebody fucked up. This fixes wolf colours incorrectly converting between versions
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:wolf", new DataConverter<>(VERSION, 5) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final Number colour = data.getNumber("CollarColor");

                if (colour != null) {
                    data.setByte("CollarColor", (byte)(15 - colour.intValue()));
                }

                return null;
            }
        });
         */
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:banner", new DataConverter<>(VERSION, 5) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final Number base = data.getNumber("Base");
                if (base != null) {
                    data.setInt("Base", 15 - base.intValue());
                }

                final ListType patterns = data.getList("Patterns", ObjectType.MAP);
                if (patterns != null) {
                    for (int i = 0, len = patterns.size(); i < len; ++i) {
                        final MapType<String> pattern = patterns.getMap(i);
                        final Number colour = pattern.getNumber("Color");
                        if (colour != null) {
                            pattern.setInt("Color", 15 - colour.intValue());
                        }
                    }
                }

                return null;
            }
        });
        MCTypeRegistry.LEVEL.addStructureConverter(new DataConverter<>(VERSION, 5) {
            private final Splitter SPLITTER = Splitter.on(';').limit(5);
            private final Splitter LAYER_SPLITTER = Splitter.on(',');
            private final Splitter OLD_AMOUNT_SPLITTER = Splitter.on('x').limit(2);
            private final Splitter AMOUNT_SPLITTER = Splitter.on('*').limit(2);
            private final Splitter BLOCK_SPLITTER = Splitter.on(':').limit(3);

            // idk man i just copy and pasted this one
            private String fixGeneratorSettings(final String generatorSettings) {
                if (generatorSettings.isEmpty()) {
                    return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
                } else {
                    Iterator<String> iterator = SPLITTER.split(generatorSettings).iterator();
                    String string2 = (String)iterator.next();
                    int j;
                    String string4;
                    if (iterator.hasNext()) {
                        j = NumberUtils.toInt(string2, 0);
                        string4 = (String)iterator.next();
                    } else {
                        j = 0;
                        string4 = string2;
                    }

                    if (j >= 0 && j <= 3) {
                        StringBuilder stringBuilder = new StringBuilder();
                        Splitter splitter = j < 3 ? OLD_AMOUNT_SPLITTER : AMOUNT_SPLITTER;
                        stringBuilder.append((String) StreamSupport.stream(LAYER_SPLITTER.split(string4).spliterator(), false).map((stringx) -> {
                            List<String> list = splitter.splitToList(stringx);
                            int k;
                            String string3;
                            if (list.size() == 2) {
                                k = NumberUtils.toInt((String)list.get(0));
                                string3 = (String)list.get(1);
                            } else {
                                k = 1;
                                string3 = (String)list.get(0);
                            }

                            List<String> list2 = BLOCK_SPLITTER.splitToList(string3);
                            int l = ((String)list2.get(0)).equals("minecraft") ? 1 : 0;
                            String string5 = (String)list2.get(l);
                            int m = j == 3 ? EntityBlockStateFix.getBlockId("minecraft:" + string5) : NumberUtils.toInt(string5, 0);
                            int n = l + 1;
                            int o = list2.size() > n ? NumberUtils.toInt((String)list2.get(n), 0) : 0;
                            return (k == 1 ? "" : k + "*") + BlockStateData.getTag(m << 4 | o).get("Name").asString("");
                        }).collect(Collectors.joining(",")));

                        while(iterator.hasNext()) {
                            stringBuilder.append(';').append((String)iterator.next());
                        }

                        return stringBuilder.toString();
                    } else {
                        return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
                    }
                }
            }

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                if (!"flat".equalsIgnoreCase(data.getString("generatorName"))) {
                    return null;
                }

                final String generatorOptions = data.getString("generatorOptions");
                if (generatorOptions == null) {
                    return null;
                }

                data.setString("generatorOptions", this.fixGeneratorSettings(generatorOptions));

                return null;
            }
        });

        // V6
        MCTypeRegistry.STATS.addStructureConverter(new ConverterFlattenStats());
        MCTypeRegistry.TILE_ENTITY.addConverterForId("minecraft:jukebox", new DataConverter<>(VERSION, 6) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final int record = data.getInt("Record");
                if (record <= 0) {
                    return null;
                }

                data.remove("Record");

                final String newItemId = ConverterFlattenItemStack.flattenItem(HelperItemNameV102.getNameFromId(record), 0);
                if (newItemId == null) {
                    return null;
                }

                final MapType<String> recordItem = Types.NBT.createEmptyMap();
                data.setMap("RecordItem", recordItem);

                recordItem.setString("id", newItemId);
                recordItem.setByte("Count", (byte)1);

                return null;
            }
        });

        MCTypeRegistry.STATS.addStructureWalker(VERSION, 6, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final MapType<String> stats = data.getMap("stats");
            if (stats == null) {
                return null;
            }

            WalkerUtils.convertKeys(MCTypeRegistry.BLOCK_NAME, stats, "minecraft:mined", fromVersion, toVersion);

            WalkerUtils.convertKeys(MCTypeRegistry.ITEM_NAME, stats, "minecraft:crafted", fromVersion, toVersion);
            WalkerUtils.convertKeys(MCTypeRegistry.ITEM_NAME, stats, "minecraft:used", fromVersion, toVersion);
            WalkerUtils.convertKeys(MCTypeRegistry.ITEM_NAME, stats, "minecraft:broken", fromVersion, toVersion);
            WalkerUtils.convertKeys(MCTypeRegistry.ITEM_NAME, stats, "minecraft:picked_up", fromVersion, toVersion);
            WalkerUtils.convertKeys(MCTypeRegistry.ITEM_NAME, stats, "minecraft:dropped", fromVersion, toVersion);

            WalkerUtils.convertKeys(MCTypeRegistry.ENTITY_NAME, stats, "minecraft:killed", fromVersion, toVersion);
            WalkerUtils.convertKeys(MCTypeRegistry.ENTITY_NAME, stats, "minecraft:killed_by", fromVersion, toVersion);

            return null;
        });

        MCTypeRegistry.OBJECTIVE.addStructureHook(VERSION, 6, new DataHook<>() {
            private static String packWithDot(final String string) {
                final ResourceLocation resourceLocation = ResourceLocation.tryParse(string);
                return resourceLocation != null ? resourceLocation.getNamespace() + "." + resourceLocation.getPath() : string;
            }

            @Override
            public MapType<String> preHook(final MapType<String> data, final long fromVersion, final long toVersion) {
                // unpack
                final String criteriaName = data.getString("CriteriaName");
                String type;
                String id;

                if (criteriaName != null) {
                    final int index = criteriaName.indexOf(':');
                    if (index < 0) {
                        type = "_special";
                        id = criteriaName;
                    } else {
                        try {
                            type = ResourceLocation.of(criteriaName.substring(0, index), '.').toString();
                            id = ResourceLocation.of(criteriaName.substring(index + 1), '.').toString();
                        } catch (final Exception ex) {
                            type = "_special";
                            id = criteriaName;
                        }
                    }
                } else {
                    type = null;
                    id = null;
                }

                if (type != null && id != null) {
                    final MapType<String> criteriaType = Types.NBT.createEmptyMap();
                    data.setMap("CriteriaType", criteriaType);

                    criteriaType.setString("type", type);
                    criteriaType.setString("id", id);
                }

                return null;
            }

            @Override
            public MapType<String> postHook(final MapType<String> data, final long fromVersion, final long toVersion) {
                // repack
                final MapType<String> criteriaType = data.getMap("CriteriaType");

                final String newName;
                if (criteriaType == null) {
                    newName = null;
                } else {
                    final String type = criteriaType.getString("type");
                    final String id = criteriaType.getString("id");
                    if (type != null && id != null) {
                        if ("_special".equals(type)) {
                            newName = id;
                        } else {
                            newName = packWithDot(type) + ":" + packWithDot(id);
                        }
                    } else {
                        newName = null;
                    }
                }

                if (newName != null) {
                    data.remove("CriteriaType");
                    data.setString("CriteriaName", newName);
                }

                return null;
            }
        });

        MCTypeRegistry.OBJECTIVE.addStructureWalker(VERSION, 6, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final MapType<String> criteriaType = data.getMap("CriteriaType");
            if (criteriaType == null) {
                return null;
            }

            final String type = criteriaType.getString("type");

            if (type == null) {
                return null;
            }

            switch (type) {
                case "minecraft:mined": {
                    WalkerUtils.convert(MCTypeRegistry.BLOCK_NAME, criteriaType, "id", fromVersion, toVersion);
                    break;
                }

                case "minecraft:crafted":
                case "minecraft:used":
                case "minecraft:broken":
                case "minecraft:picked_up":
                case "minecraft:dropped": {
                    WalkerUtils.convert(MCTypeRegistry.ITEM_NAME, criteriaType, "id", fromVersion, toVersion);
                    break;
                }

                case "minecraft:killed":
                case "minecraft:killed_by": {
                    WalkerUtils.convert(MCTypeRegistry.ENTITY_NAME, criteriaType, "id", fromVersion, toVersion);
                    break;
                }
            }

            return null;
        });


        // V7
        MCTypeRegistry.STRUCTURE_FEATURE.addStructureConverter(new DataConverter<>(VERSION, 7) {
            private static void convertToBlockState(final MapType<String> data, final String path) {
                final Number number = data.getNumber(path);
                if (number == null) {
                    return;
                }

                data.setMap(path, HelperBlockFlatteningV1450.getNBTForId(number.intValue() << 4).copy()); // copy to avoid problems with later state datafixers
            }

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final ListType children = data.getList("Children", ObjectType.MAP);
                if (children == null) {
                    return null;
                }

                for (int i = 0, len = children.size(); i < len; ++i) {
                    final MapType<String> child = children.getMap(i);

                    final String id = child.getString("id");

                    switch (id) {
                        case "ViF":
                            convertToBlockState(child, "CA");
                            convertToBlockState(child, "CB");
                            break;
                        case "ViDF":
                            convertToBlockState(child, "CA");
                            convertToBlockState(child, "CB");
                            convertToBlockState(child, "CC");
                            convertToBlockState(child, "CD");
                            break;
                    }
                }

                return null;
            }
        });

        // convert villagers to trade with pumpkins and not the carved pumpkin
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:villager", new DataConverter<>(VERSION, 7) {
            private static void convertPumpkin(final MapType<String> data, final String path) {
                final MapType<String> item = data.getMap(path);
                if (item == null) {
                    return;
                }

                final String id = item.getString("id");

                if (id.equals("minecraft:carved_pumpkin")) {
                    item.setString("id", "minecraft:pumpkin");
                }
            }

            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> offers = data.getMap("Offers");
                if (offers != null) {
                    final ListType recipes = offers.getList("Recipes", ObjectType.MAP);
                    if (recipes != null) {
                        for (int i = 0, len = recipes.size(); i < len; ++i) {
                            final MapType<String> recipe = recipes.getMap(i);

                            convertPumpkin(recipe, "buy");
                            convertPumpkin(recipe, "buyB");
                            convertPumpkin(recipe, "sell");
                        }
                    }
                }
                return null;
            }
        });

        MCTypeRegistry.STRUCTURE_FEATURE.addStructureWalker(VERSION, 7, (final MapType<String> data, final long fromVersion, final long toVersion) -> {
            final ListType list = data.getList("Children", ObjectType.MAP);
            if (list == null) {
                return null;
            }

            for (int i = 0, len = list.size(); i < len; ++i) {
                final MapType<String> child = list.getMap(i);
                WalkerUtils.convert(MCTypeRegistry.BLOCK_STATE, child, "CA", fromVersion, toVersion);
                WalkerUtils.convert(MCTypeRegistry.BLOCK_STATE, child, "CB", fromVersion, toVersion);
                WalkerUtils.convert(MCTypeRegistry.BLOCK_STATE, child, "CC", fromVersion, toVersion);
                WalkerUtils.convert(MCTypeRegistry.BLOCK_STATE, child, "CD", fromVersion, toVersion);
            }

            return null;
        });
    }

    private V1451() {}
}
