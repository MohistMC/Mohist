package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.DataWalkerTypePaths;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItems;

public final class V3325 {

    private static final int VERSION = MCVersions.V23W05A + 2;

    public static void register() {
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:item_display", new DataWalkerItems("item"));
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:block_display", new DataWalkerTypePaths<>(MCTypeRegistry.BLOCK_STATE, "block_state"));
        // text_display is a simple entity
    }
}
