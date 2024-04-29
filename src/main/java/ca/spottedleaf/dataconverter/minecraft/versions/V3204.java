package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;

public final class V3204 {

    private static final int VERSION = MCVersions.V1_19_2 + 84;

    public static void register() {
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:chiseled_bookshelf", new DataWalkerItemLists("Items"));
    }
}
