package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.helpers.AddFlagIfAbsent;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;

public final class V2825 {

    protected static final int VERSION = MCVersions.V1_17_1 + 95;

    public static void register() {
        MCTypeRegistry.WORLD_GEN_SETTINGS.addStructureConverter(new AddFlagIfAbsent(VERSION, "has_increased_height_already", false));
    }

}
