package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItemLists;
import ca.spottedleaf.dataconverter.minecraft.walkers.itemstack.DataWalkerItems;

public final class V1906 {

    protected static final int VERSION = MCVersions.V18W43C + 3;

    private V1906() {}

    public static void register() {
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:barrel", new DataWalkerItemLists("Items"));
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:smoker", new DataWalkerItemLists("Items"));
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:blast_furnace", new DataWalkerItemLists("Items"));
        MCTypeRegistry.TILE_ENTITY.addWalker(VERSION, "minecraft:lectern", new DataWalkerItems("Book"));
    }

}
