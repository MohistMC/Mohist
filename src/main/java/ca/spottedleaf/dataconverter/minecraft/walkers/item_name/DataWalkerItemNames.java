package ca.spottedleaf.dataconverter.minecraft.walkers.item_name;

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.DataWalkerTypePaths;

public final class DataWalkerItemNames extends DataWalkerTypePaths<Object, Object> {

    public DataWalkerItemNames(final String... paths) {
        super(MCTypeRegistry.ITEM_NAME, paths);
    }
}
