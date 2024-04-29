package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.ConverterAbstractStringValueTypeRename;
import ca.spottedleaf.dataconverter.minecraft.converters.leveldat.ConverterRemoveFeatureFlag;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.util.NamespaceUtil;

import java.util.Arrays;
import java.util.HashSet;

public final class V3440 {

    private static final int VERSION = MCVersions.V1_19_4 + 103;

    public static void register() {
        // Note: MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST is namespaced string
        ConverterAbstractStringValueTypeRename.register(VERSION, MCTypeRegistry.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, (final String in) -> {
            return "minecraft:overworld_update_1_20".equals(NamespaceUtil.correctNamespace(in)) ? "minecraft:overworld" : null;
        });
        MCTypeRegistry.LEVEL.addStructureConverter(new ConverterRemoveFeatureFlag(VERSION, new HashSet<>(
                Arrays.asList(
                        "minecraft:update_1_20"
                )
        )));
    }
}
