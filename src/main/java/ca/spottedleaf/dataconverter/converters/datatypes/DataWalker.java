package ca.spottedleaf.dataconverter.converters.datatypes;

import ca.spottedleaf.dataconverter.types.MapType;

public interface DataWalker<K> {

    public MapType<String> walk(final MapType<K> data, final long fromVersion, final long toVersion);

}
