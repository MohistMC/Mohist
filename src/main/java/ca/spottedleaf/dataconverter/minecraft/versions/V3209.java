package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.converters.itemstack.ConverterFlattenSpawnEgg;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;

public final class V3209 {

    private static final int VERSION = MCVersions.V22W45A + 1;

    public static void register() {
        // Note: This converter reads entity id from its sub data, but we need no breakpoint because entity ids are not
        // remapped this version
        MCTypeRegistry.ITEM_STACK.addConverterForId("minecraft:pig_spawn_egg", new ConverterFlattenSpawnEgg(VERSION, 0));
    }
}
