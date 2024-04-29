package ca.spottedleaf.dataconverter.minecraft.walkers.generic;

import ca.spottedleaf.dataconverter.converters.datatypes.DataType;
import ca.spottedleaf.dataconverter.converters.datatypes.DataWalker;
import ca.spottedleaf.dataconverter.types.MapType;

public class DataWalkerTypePaths<T, R> implements DataWalker<String> {

    protected final DataType<T, R> type;
    protected final String[] paths;

    public DataWalkerTypePaths(final DataType<T, R> type, final String... paths) {
        this.type = type;
        this.paths = paths;
    }

    @Override
    public final MapType<String> walk(final MapType<String> data, final long fromVersion, final long toVersion) {
        for (final String path : this.paths) {
            final Object current = data.getGeneric(path);
            if (current == null) {
                continue;
            }

            final Object converted = this.type.convert((T)current, fromVersion, toVersion);

            if (converted != null) {
                data.setGeneric(path, converted);
            }
        }

        return null;
    }
}
