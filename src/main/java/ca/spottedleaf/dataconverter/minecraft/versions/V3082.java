package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;

public final class V3082 {

    protected static final int VERSION = MCVersions.V22W11A + 2;

    public static void register() {
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:chest_boat", new DataWalkerItemLists("Items"));
    }
}
