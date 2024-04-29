package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.chunk.ConverterAddBlendingData;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;

public final class V3441 {

    private static final int VERSION = MCVersions.V1_19_4 + 104;

    public static void register() {
        // See V3088 for why this converter is duplicated here and in V3088
        MCTypeRegistry.CHUNK.addStructureConverter(new ConverterAddBlendingData(VERSION));
    }
}
