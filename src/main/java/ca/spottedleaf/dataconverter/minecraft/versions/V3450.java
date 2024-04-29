package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.chunk.ConverterRenameStatus;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import java.util.HashMap;
import java.util.Map;

public final class V3450 {

    private static final int VERSION = MCVersions.V23W16A + 1;

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new ConverterRenameStatus(VERSION, new HashMap<>(
                Map.of(
                        "minecraft:liquid_carvers", "minecraft:carvers",
                        "minecraft:heightmaps", "minecraft:spawn"
                )
        )::get));
    }
}
