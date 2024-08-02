package ca.spottedleaf.dataconverter.minecraft.walkers.block_name;

import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.minecraft.walkers.generic.DataWalkerTypePaths;

public final class DataWalkerBlockNames extends DataWalkerTypePaths<Object, Object> {

    public DataWalkerBlockNames(final String... paths) {
        super(MCTypeRegistry.BLOCK_NAME, paths);
    }
}
