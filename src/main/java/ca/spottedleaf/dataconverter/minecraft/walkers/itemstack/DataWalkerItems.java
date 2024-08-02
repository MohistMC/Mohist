package ca.spottedleaf.dataconverter.minecraft.walkers.itemstack;

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.DataWalkerTypePaths;
import ca.spottedleaf.dataconverter.types.MapType;

public class DataWalkerItems extends DataWalkerTypePaths<MapType<String>, MapType<String>> {

    public DataWalkerItems(final String... paths) {
        super(MCTypeRegistry.ITEM_STACK, paths);
    }
}
