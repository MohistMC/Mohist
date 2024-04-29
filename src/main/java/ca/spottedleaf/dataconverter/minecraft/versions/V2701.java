package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class V2701 {

    protected static final int VERSION = MCVersions.V21W10A + 2;

    private static final Pattern INDEX_PATTERN = Pattern.compile("\\[(\\d+)\\]");

    private static final Set<String> PIECE_TYPE = Sets.newHashSet(
            "minecraft:jigsaw",
            "minecraft:nvi",
            "minecraft:pcp",
            "minecraft:bastionremnant",
            "minecraft:runtime"
    );
    private static final Set<String> FEATURES = Sets.newHashSet(
            "minecraft:tree",
            "minecraft:flower",
            "minecraft:block_pile",
            "minecraft:random_patch"
    );

    public static void register() {
        MCTypeRegistry.STRUCTURE_FEATURE.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final ListType children = data.getList("Children", ObjectType.MAP);

                if (children == null) {
                    return null;
                }

                for (int i = 0, len = children.size(); i < len; ++i) {
                    final MapType<String> child = children.getMap(i);

                    if (!PIECE_TYPE.contains(child.getString("id"))) {
                        continue;
                    }

                    final String poolElement = child.getString("pool_element");
                    if (!"minecraft:feature_pool_element".equals(poolElement)) {
                        continue;
                    }

                    final MapType<String> feature = child.getMap("feature");
                    if (feature == null) {
                        continue;
                    }

                    final String replacement = convertToString(feature);

                    if (replacement != null) {
                        child.setString("feature", replacement);
                    }
                }

                return null;
            }
        });
    }

    private static String getNestedString(final MapType<String> root, final String... paths) {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Missing path");
        }

        Object current = root.getGeneric(paths[0]);

        for (int i = 1; i < paths.length; ++i) {
            final String path = paths[i];

            final Matcher indexMatcher = INDEX_PATTERN.matcher(path);
            if (!indexMatcher.matches()) {
                current = (current instanceof MapType) ? ((MapType<String>)current).getGeneric(path) : null;
                if (current == null) {
                    break;
                }
                continue;
            }

            final int index = Integer.parseInt(indexMatcher.group(1));
            if (!(current instanceof ListType)) {
                current = null;
                break;
            } else {
                final ListType list = (ListType)current;
                if (index >= 0 && index < list.size()) {
                    current = list.getGeneric(index);
                } else {
                    current = null;
                    break;
                }
            }
        }

        return current instanceof String ? (String)current : "";
    }

    protected static String convertToString(final MapType<String> feature) {
        return getReplacement(
                getNestedString(feature, "type"),
                getNestedString(feature, "name"),
                getNestedString(feature, "config", "state_provider", "type"),
                getNestedString(feature, "config", "state_provider", "state", "Name"),
                getNestedString(feature, "config", "state_provider", "entries", "[0]", "data", "Name"),
                getNestedString(feature, "config", "foliage_placer", "type"),
                getNestedString(feature, "config", "leaves_provider", "state", "Name")
        );
    }

    private static String getReplacement(final String type, final String name, final String stateType, final String stateName,
                                         final String firstEntryName, final String foliageName, final String leavesName) {
        final String actualType;
        if (!type.isEmpty()) {
            actualType = type;
        } else {
            if (name.isEmpty()) {
                return null;
            }

            if ("minecraft:normal_tree".equals(name)) {
                actualType = "minecraft:tree";
            } else {
                actualType = name;
            }
        }

        if (FEATURES.contains(actualType)) {
            if ("minecraft:random_patch".equals(actualType)) {
                if ("minecraft:simple_state_provider".equals(stateType)) {
                    if ("minecraft:sweet_berry_bush".equals(stateName)) {
                        return "minecraft:patch_berry_bush";
                    }

                    if ("minecraft:cactus".equals(stateName)) {
                        return "minecraft:patch_cactus";
                    }
                } else if ("minecraft:weighted_state_provider".equals(stateType) && ("minecraft:grass".equals(firstEntryName) || "minecraft:fern".equals(firstEntryName))) {
                    return "minecraft:patch_taiga_grass";
                }
            } else if ("minecraft:block_pile".equals(actualType)) {
                if (!"minecraft:simple_state_provider".equals(stateType) && !"minecraft:rotated_block_provider".equals(stateType)) {
                    if ("minecraft:weighted_state_provider".equals(stateType)) {
                        if ("minecraft:packed_ice".equals(firstEntryName) || "minecraft:blue_ice".equals(firstEntryName)) {
                            return "minecraft:pile_ice";
                        }

                        if ("minecraft:jack_o_lantern".equals(firstEntryName) || "minecraft:pumpkin".equals(firstEntryName)) {
                            return "minecraft:pile_pumpkin";
                        }
                    }
                } else {
                    if ("minecraft:hay_block".equals(stateName)) {
                        return "minecraft:pile_hay";
                    }

                    if ("minecraft:melon".equals(stateName)) {
                        return "minecraft:pile_melon";
                    }

                    if ("minecraft:snow".equals(stateName)) {
                        return "minecraft:pile_snow";
                    }
                }
            } else {
                if ("minecraft:flower".equals(actualType)) {
                    return "minecraft:flower_plain";
                }

                if ("minecraft:tree".equals(actualType)) {
                    if ("minecraft:acacia_foliage_placer".equals(foliageName)) {
                        return "minecraft:acacia";
                    }

                    if ("minecraft:blob_foliage_placer".equals(foliageName) && "minecraft:oak_leaves".equals(leavesName)) {
                        return "minecraft:oak";
                    }

                    if ("minecraft:pine_foliage_placer".equals(foliageName)) {
                        return "minecraft:pine";
                    }

                    if ("minecraft:spruce_foliage_placer".equals(foliageName)) {
                        return "minecraft:spruce";
                    }
                }
            }
        }

        return null;
    }
}
